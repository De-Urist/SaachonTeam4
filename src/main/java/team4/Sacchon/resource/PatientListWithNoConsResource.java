package team4.Sacchon.resource;

import org.restlet.resource.Get;

import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.ConsultationRepository;
import team4.Sacchon.representation.PatientRepresentation;
import team4.Sacchon.security.Shield;
import team4.Sacchon.repository.DoctorRepository;
import team4.Sacchon.repository.PatientRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;


public class PatientListWithNoConsResource extends ServerResource {

    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Get("json")
    public ApiResult<Object> getPatientsWithNoCons() {
        ApiResult<Object> privileges = checkDoctorPrivileges();
        if (privileges != null)
            return privileges;


        EntityManager em = JpaUtil.getEntityManager();
        List<Patient> allPatients = new PatientRepository(em).getPatientsofDoctorOrNoDoctor(id);
        List<Patient> consultedPatients = new ConsultationRepository(em).getPatientsWithConsultations();
        allPatients.removeAll(consultedPatients);
        PatientRepository patientRepository = new PatientRepository(em);
        List<PatientRepresentation> eligiblePatients = new ArrayList<>();
        for (Patient p : allPatients) {
            if (p.getDoctor() == null || p.getDoctor().getId() == id){
                if (patientRepository.canBeAdvised(p.getId())){
                    eligiblePatients.add(new PatientRepresentation(p));
                }
            }
        }
        em.close();
        if (eligiblePatients.size() == 0) {
            return new ApiResult<>(null, 400, "No available patients for consultations.");
        }
        return new ApiResult<>(eligiblePatients, 200, "All available patients for consultations.");
    }

    private ApiResult<Object> checkDoctorPrivileges() {
        try {
            ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        } catch (AuthorizationException e) {
            return new ApiResult<>(null, 403, "Forbidden");
        }
        return null;
    }
}



package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.repository.DoctorRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.PatientRepresentation;
import team4.Sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorPatientListResource extends ServerResource {

    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Get("json")
    public ApiResult<Object> getDoctorAssignedAndUnassignedPatients() {
        ApiResult<Object> privileges = checkDoctorPrivileges();
        if (privileges != null)
            return privileges;

        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        List<Patient> patients = doctorRepository.getAllAvailablePatients(id);
        em.close();
        if (patients.size() != 0) {
            List<PatientRepresentation> patientRepresentations = patients.stream()
                    .map(PatientRepresentation::new)
                    .collect(Collectors.toList());
            return new ApiResult<>(patientRepresentations, 200, "All available patients");
        }
        return new ApiResult<>(null, 400, "No available patients");
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

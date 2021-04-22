package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.PatientRepresentation;
import team4.Sacchon.security.Shield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaitingPatientsResource extends ServerResource {

    @Get("json")
    public ApiResult<Object> getWaitingPatients() {
        ApiResult<Object> privileges = checkChiefDoctorPrivileges();
        if (privileges != null)
            return privileges;

        List<Object[]> patientAwaitingConsultation = new ChiefDoctorRepository(JpaUtil.getEntityManager()).getPatientAwaitingConsultation();
        List<PatientRepresentation> patientRepresentations = new ArrayList<>();
        PatientRepository patientRepository = new PatientRepository(JpaUtil.getEntityManager());
        for (Object[] p : patientAwaitingConsultation) {
            int value  = (int) p[0];
            Patient patient = patientRepository.read(value);
            patientRepresentations.add(new PatientRepresentation(patient));
        }
        if (patientRepresentations.size() != 0)
            return new ApiResult<>(patientRepresentations, 200, "Patients waiting for consultations.");
        else
            return new ApiResult<>(null, 400, "No patients are waiting for consultations");
    }

    private ApiResult<Object> checkChiefDoctorPrivileges(){
        try {
            ResourceUtils.checkRole(this, Shield.ROLE_CHIEF);
        }catch (AuthorizationException e) {
            return new ApiResult<>(null, 403, "Forbidden");
        }
        return null;
    }

}

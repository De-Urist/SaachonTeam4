package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.PatientRepresentation;
import team4.Sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class PatientListResource extends ServerResource {

    @Get("json")
    public ApiResult<Object> getPatients() {
        ApiResult<Object> privileges = checkChiefDoctorPrivileges();
        if (privileges != null)
            return privileges;

        EntityManager em = JpaUtil.getEntityManager();
        List<Patient> patients = new PatientRepository(em).findAll();
        em.close();
        List<PatientRepresentation> patientRepresentations = patients.stream()
                .map(PatientRepresentation::new)
                .collect(Collectors.toList());
        return new ApiResult<>(patientRepresentations, 200, "All available customers");
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
package team4.Sacchon.resource;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Credentials;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.CredentialsRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.PatientRepresentation;

import javax.persistence.EntityManager;

public class PatientResource extends ServerResource {
    private String username;

    @Override
    protected void doInit() {
        username = getAttribute("username");
    }

    @Get("json")
    public ApiResult<PatientRepresentation> getPatient() {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.getByUsername(username);

        PatientRepresentation patientRepresentation = new PatientRepresentation(patient);
        em.close();
        return new ApiResult<>(patientRepresentation, 200, "Found patient");
    }

    @Delete("json")
    public ApiResult<Boolean> deletePatient() {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.getByUsername(username);
        boolean result = patientRepository.delete(patient.getId());
        if (result){
            CredentialsRepository credentialsRepository = new CredentialsRepository(em);
            Credentials cred = credentialsRepository.getByUsername(username);
            credentialsRepository.delete(cred.getId());
        }
        int code = (result ? 200 : 400);
        String description = (result ? "Patient deleted" : "Patient could not be deleted");
        return new ApiResult<>(result, code, description);
    }
}

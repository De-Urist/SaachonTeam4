package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Credentials;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.CredentialsRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.PatientRepresentation;

import javax.persistence.EntityManager;

import static team4.Sacchon.resource.ResourceUtils.checkPatientInformation;
import static team4.Sacchon.resource.ResourceUtils.usernameExistsInCredentials;

public class RegisterPatientResource extends ServerResource {

    @Post("json")
    public ApiResult<Object> registerPatient(PatientRepresentation patientRepresentation) {
        ApiResult<Object> checkedPatient = checkPatientInformation(patientRepresentation);
        if (checkedPatient != null)
            return checkedPatient;

        Patient patient = createPatientFromRepresantation(patientRepresentation);
        if (patient != null) {
            return new ApiResult<>(new PatientRepresentation(patient), 200, "The patient was successfully created");
        }
        return new ApiResult<>(null, 400, "The patient could not be created.");
    }

    private Patient createPatientFromRepresantation(PatientRepresentation patientRepresentation) {
        try {
            EntityManager em = JpaUtil.getEntityManager();
            PatientRepository patientRepository = new PatientRepository(em);
            Patient patient = patientRepresentation.createPatient();
            patientRepository.save(patient);
            new CredentialsRepository(em).save(new Credentials(patient.getUsername()));
            em.close();
            return patient;
        } catch (Exception e) {
            return null;
        }
    }

    @Get("json")
    public boolean usernameExists() {
        String candidateUsername;
        try {
            candidateUsername = getQueryValue("username");
        } catch (Exception e) {
            return false;
        }
        return usernameExistsInCredentials(candidateUsername);
    }
}

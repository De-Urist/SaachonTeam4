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

import static team4.Sacchon.resource.ResourceUtils.usernameExistsInCredentials;

public class RegisterPatientResource extends ServerResource {

    @Post("json")
    public ApiResult<PatientRepresentation> registerPatient(PatientRepresentation patientRepresentation) {
        ApiResult<PatientRepresentation> checkedPatient = checkPatientInformation(patientRepresentation);
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

    private ApiResult<PatientRepresentation> checkPatientInformation(PatientRepresentation patientRepresentation) {
        if (patientRepresentation == null)
            return new ApiResult<>(null, 400, "No input data were given to create the patient");
        if (patientRepresentation.getName() == null)
            return new ApiResult<>(null, 400, "No name was given to create the patient");
        if (patientRepresentation.getUsername() == null)
            return new ApiResult<>(null, 400, "No username was given to create the patient");
        if (patientRepresentation.getPassword() == null)
            return new ApiResult<>(null, 400, "No password was given to create the patient");
        if (patientRepresentation.getRole() == null || !patientRepresentation.getRole().equals("patient"))
            return new ApiResult<>(null, 400, "Incorrect role was passed in the patient creation");
        if (usernameExistsInCredentials(patientRepresentation.getUsername()))
            return new ApiResult<>(null, 400, "Duplicate username");
        return null;
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

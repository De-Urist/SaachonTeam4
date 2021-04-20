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

public class RegisterPatientResource extends ServerResource {

    @Post("json")
    public ApiResult<PatientRepresentation> registerPatient(PatientRepresentation patientRepresentation) {
        ApiResult<PatientRepresentation> checkedPatient = checkPatientInformation(patientRepresentation);
        if (checkedPatient != null)
            return checkedPatient;

        Patient patient = createPatientFromRepresantation(patientRepresentation);
        if (patient != null) {
            return new ApiResult<>(new PatientRepresentation(patient), 200, "The patient was successfully created");
        } else {
            return new ApiResult<>(null, 400, "The patient could not be created.");
        }
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
        if (usernameExists(patientRepresentation.getUsername()))
            return new ApiResult<>(null, 400, "Duplicate username");
        return null;
    }

    private boolean usernameExists(String candidateUsername) {
        EntityManager em = JpaUtil.getEntityManager();
        CredentialsRepository credentialsRepository = new CredentialsRepository(em);
        Credentials credentials = credentialsRepository.getByUsername(candidateUsername);
        em.close();
        return credentials != null;
    }

    @Get("json")
    public boolean usernameExists() {
        String candidateUsername;
        try {
            candidateUsername = getQueryValue("username");
        } catch (Exception e) {
            return false;
        }
        return usernameExists(candidateUsername);
    }
}

package team4.Sacchon.resource;

import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Credentials;
import team4.Sacchon.repository.CredentialsRepository;
import team4.Sacchon.representation.DoctorRepresentation;
import team4.Sacchon.representation.MeasurementRepresentation;
import team4.Sacchon.representation.PatientRepresentation;

import javax.persistence.EntityManager;

public class ResourceUtils {

    public static void checkRole(ServerResource serverResource, String role) throws AuthorizationException {
        if (!serverResource.isInRole(role)) {
            throw new AuthorizationException("You're not authorized to send this call.");
        }
    }

    protected static boolean usernameExistsInCredentials(String candidateUsername) {
        EntityManager em = JpaUtil.getEntityManager();
        CredentialsRepository credentialsRepository = new CredentialsRepository(em);
        Credentials credentials = credentialsRepository.getByUsername(candidateUsername);
        em.close();
        return credentials != null;
    }

    protected static ApiResult<Object> checkPatientInformation(PatientRepresentation patientRepresentation) {
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

    protected static ApiResult<Object> checkMeasurementInformation(MeasurementRepresentation measurementRepresentation) {
        if (measurementRepresentation == null)
            return new ApiResult<>(null, 400, "No input data were given to create the measurement");
        if (measurementRepresentation.getDate() == null)
            return new ApiResult<>(null, 400, "No date was given to create the measurement");
        if (measurementRepresentation.getTime() == null)
            return new ApiResult<>(null, 400, "No time was given to create the measurement");
        if (measurementRepresentation.getGlucoseLevel() == 0)
            return new ApiResult<>(null, 400, "No glucose level data were given to create the measurement");
        if (measurementRepresentation.getCarbIntake() == 0)
            return new ApiResult<>(null, 400, "No carb intake data were given to create the measurement");
        return null;
    }

    protected static ApiResult<Object> checkDoctorInformation(DoctorRepresentation doctorRepresentation) {
        if (doctorRepresentation == null)
            return new ApiResult<>(null, 400, "No input data were given to create the doctor");
        if (doctorRepresentation.getName() == null)
            return new ApiResult<>(null, 400, "No name was given to create the doctor");
        if (doctorRepresentation.getUsername() == null)
            return new ApiResult<>(null, 400, "No username was given to create the doctor");
        if (doctorRepresentation.getPassword() == null)
            return new ApiResult<>(null, 400, "No password was given to create the doctor");
        if (doctorRepresentation.getRole() == null || !doctorRepresentation.getRole().equals("doctor"))
            return new ApiResult<>(null, 400, "Incorrect role was passed in the doctor creation");
        if (usernameExistsInCredentials(doctorRepresentation.getUsername()))
            return new ApiResult<>(null, 400, "Duplicate username");
        return null;
    }
}
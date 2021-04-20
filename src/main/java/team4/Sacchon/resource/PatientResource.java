package team4.Sacchon.resource;

import org.restlet.resource.*;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Credentials;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.CredentialsRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.DateTimeRepresantation;
import team4.Sacchon.representation.PatientRepresentation;
import team4.Sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static team4.Sacchon.resource.ResourceUtils.checkPatientInformation;

public class PatientResource extends ServerResource {
    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Delete("json")
    public ApiResult<Object> deletePatient() {
        ApiResult<Object> privileges = checkPatientPrivileges();
        if (privileges != null)
            return privileges;

        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        String patientUsername = patientRepository.read(id).getUsername();
        boolean patientDeletionResult = patientRepository.delete(id);
        boolean credentialsDeletionResult = false;
        if (patientDeletionResult){
            CredentialsRepository credentialsRepository = new CredentialsRepository(em);
            Credentials cred = credentialsRepository.getByUsername(patientUsername);
            credentialsDeletionResult = credentialsRepository.delete(cred.getId());
        }
        int code = (patientDeletionResult && credentialsDeletionResult) ? 200 : 400;
        String description = (patientDeletionResult ? "Patient deleted" : "Patient could not be deleted");
        return new ApiResult<>(patientDeletionResult, code, description);
    }

    @Put("json")
    public ApiResult<Object> changePatientInfo(PatientRepresentation patientRepresentation) {
        ApiResult<Object> privileges = checkPatientPrivileges();
        if (privileges != null)
            return privileges;

        ApiResult<Object> checkedPatient = checkPatientInformation(patientRepresentation);
        if (checkedPatient != null)
            return checkedPatient;

        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient currentPatient = patientRepository.read(id);
        String currentUsername = currentPatient.getUsername();
        currentPatient.setName(patientRepresentation.getName());
        currentPatient.setUsername(patientRepresentation.getUsername());
        currentPatient.setPassword(patientRepresentation.getPassword());
        patientRepository.update(currentPatient);

        CredentialsRepository credentialsRepository = new CredentialsRepository(em);
        Credentials credentials = credentialsRepository.getByUsername(currentUsername);
        credentials.setUsername(patientRepresentation.getUsername());
        credentialsRepository.update(credentials);
        return new ApiResult<>(patientRepresentation, 200, "Update successful");
    }

    @Patch("json")
    public ApiResult<Object> updatePatientLastLogin(DateTimeRepresantation dateTimeRepresantation) {
        ApiResult<Object> privileges = checkPatientPrivileges();
        if (privileges != null)
            return privileges;

        Date date;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateTimeRepresantation.getDate() + " " + dateTimeRepresantation.getTime() + ":00");
        } catch (ParseException e) {
            return new ApiResult<>(null, 400, "Invalid date given.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient currentPatient = patientRepository.read(id);
        currentPatient.setLastLogin(date);
        patientRepository.update(currentPatient);
        return new ApiResult<>(new PatientRepresentation(currentPatient), 200, "Update successfull");
    }

    private ApiResult<Object> checkPatientPrivileges(){
        try {
            ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        }catch (AuthorizationException e) {
            return new ApiResult<>(null, 403, "Forbidden");
        }
        return null;
    }
}

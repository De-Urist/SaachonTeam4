package team4.Sacchon.resource;

import org.restlet.resource.*;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Credentials;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.CredentialsRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.DateTimeRepresantation;
import team4.Sacchon.representation.PatientRepresentation;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Put("json")
    public ApiResult<PatientRepresentation> changePatientInfo(PatientRepresentation patientRepresentation) {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient currentPatient = patientRepository.getByUsername(username);
        currentPatient.setName(patientRepresentation.getName());
        currentPatient.setUsername(patientRepresentation.getUsername());
        currentPatient.setPassword(patientRepresentation.getPassword());
        patientRepository.update(currentPatient);

        CredentialsRepository credentialsRepository = new CredentialsRepository(em);
        Credentials credentials = credentialsRepository.getByUsername(username);
        credentials.setUsername(patientRepresentation.getUsername());
        credentialsRepository.update(credentials);
        return new ApiResult<>(patientRepresentation, 200, "Update successfull");
    }

    @Patch("json")
    public ApiResult<PatientRepresentation> updatePatientLastLogin(DateTimeRepresantation dateTimeRepresantation) {
        Date date;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateTimeRepresantation.getDate() + " " + dateTimeRepresantation.getTime() + ":00");
        } catch (ParseException e) {
            return new ApiResult<>(null, 400, "Invalid date given.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient currentPatient = patientRepository.getByUsername(username);
        currentPatient.setLastLogin(date);
        patientRepository.update(currentPatient);
        return new ApiResult<>(new PatientRepresentation(currentPatient), 200, "Update successfull");
    }
}

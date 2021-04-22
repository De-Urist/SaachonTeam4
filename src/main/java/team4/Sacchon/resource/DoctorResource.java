package team4.Sacchon.resource;

import org.restlet.resource.*;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Credentials;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.CredentialsRepository;
import team4.Sacchon.repository.DoctorRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.DateTimeRepresantation;
import team4.Sacchon.representation.DoctorRepresentation;
import team4.Sacchon.security.Shield;
;
import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static team4.Sacchon.resource.ResourceUtils.checkDoctorInformation;


public class DoctorResource extends ServerResource {
    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Delete("json")
    public ApiResult<Object> deleteDoctor() {
        ApiResult<Object> privileges = checkDoctorPrivileges();
        if (privileges != null)
            return privileges;

        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        Doctor doctor = doctorRepository.read(id);
        String doctorUsername = doctor.getUsername();
        PatientRepository patientRepository = new PatientRepository(em);
        for (Patient p : doctor.getPatients()) {
            p.setDoctor(null);
            patientRepository.update(p);
        }
        doctor.setUsername(null);
        doctorRepository.update(doctor);
        CredentialsRepository credentialsRepository = new CredentialsRepository(em);
        Credentials cred = credentialsRepository.getByUsername(doctorUsername);
        boolean credentialsDeletionResult = credentialsRepository.delete(cred.getId());

        int code = (credentialsDeletionResult) ? 200 : 400;
        String description = (credentialsDeletionResult ? "Doctor deleted" : "Doctor could not be deleted");
        return new ApiResult<>(credentialsDeletionResult, code, description);
    }

    @Put("json")
    public ApiResult<Object> changeDoctorInfo(DoctorRepresentation doctorRepresentation) {
        ApiResult<Object> privileges = checkDoctorPrivileges();
        if (privileges != null)
            return privileges;

        ApiResult<Object> checkedDoctor = checkDoctorInformation(doctorRepresentation);
        if (checkedDoctor != null)
            return checkedDoctor;

        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        Doctor currentDoctor = doctorRepository.read(id);
        String currentUsername = currentDoctor.getUsername();
        currentDoctor.setName(doctorRepresentation.getName());
        currentDoctor.setUsername(doctorRepresentation.getUsername());
        currentDoctor.setPassword(doctorRepresentation.getPassword());
        doctorRepository.update(currentDoctor);

        CredentialsRepository credentialsRepository = new CredentialsRepository(em);
        Credentials credentials = credentialsRepository.getByUsername(currentUsername);
        credentials.setUsername(doctorRepresentation.getUsername());
        credentialsRepository.update(credentials);
        return new ApiResult<>(doctorRepresentation, 200, "Update successful");
    }

    @Patch("json")
    public ApiResult<Object> updateDoctorLastLogin(DateTimeRepresantation dateTimeRepresantation) {
        ApiResult<Object> privileges = checkDoctorPrivileges();
        if (privileges != null)
            return privileges;

        Date date;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateTimeRepresantation.getDate() + " " + dateTimeRepresantation.getTime() + ":00");
        } catch (ParseException e) {
            return new ApiResult<>(null, 400, "Invalid date given.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        Doctor currentDoctor = doctorRepository.read(id);
        currentDoctor.setLastLogin(date);
        doctorRepository.update(currentDoctor);
        return new ApiResult<>(new DoctorRepresentation(currentDoctor), 200, "Update successfull");
    }





    private ApiResult<Object> checkDoctorPrivileges(){
        try {
            ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        }catch (AuthorizationException e) {
            return new ApiResult<>(null, 403, "Forbidden");
        }
        return null;
    }
}

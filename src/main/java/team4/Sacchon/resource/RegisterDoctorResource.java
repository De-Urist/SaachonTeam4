package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Credentials;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.repository.CredentialsRepository;
import team4.Sacchon.repository.DoctorRepository;
import team4.Sacchon.representation.DoctorRepresentation;

import javax.persistence.EntityManager;

import static team4.Sacchon.resource.ResourceUtils.usernameExistsInCredentials;

public class RegisterDoctorResource extends ServerResource {

    @Post("json")
    public ApiResult<DoctorRepresentation> registerDoctor(DoctorRepresentation doctorRepresentation) {
        ApiResult<DoctorRepresentation> checkedDoctor = checkDoctorInformation(doctorRepresentation);
        if (checkedDoctor != null)
            return checkedDoctor;

        Doctor doctor = createDoctorFromRepresantation(doctorRepresentation);
        if (doctor != null) {
            return new ApiResult<>(new DoctorRepresentation(doctor), 200, "The doctor was successfully created");
        }
        return new ApiResult<>(null, 400, "The doctor could not be created.");
    }

    private Doctor createDoctorFromRepresantation(DoctorRepresentation doctorRepresentation) {
        try {
            EntityManager em = JpaUtil.getEntityManager();
            DoctorRepository doctorRepository = new DoctorRepository(em);
            Doctor doctor = doctorRepresentation.createDoctor();
            doctorRepository.save(doctor);
            new CredentialsRepository(em).save(new Credentials(doctor.getUsername()));
            em.close();
            return doctor;
        } catch (Exception e) {
            return null;
        }
    }

    private ApiResult<DoctorRepresentation> checkDoctorInformation(DoctorRepresentation doctorRepresentation) {
        if (doctorRepresentation == null)
            return new ApiResult<>(null, 400, "No input data were given to create the doctor");
        if (doctorRepresentation.getName() == null)
            return new ApiResult<>(null, 400, "No name was given to create the doctor");
        if (doctorRepresentation.getUsername() == null)
            return new ApiResult<>(null, 400, "No username was given to create the doctor");
        if (doctorRepresentation.getPassword() == null)
            return new ApiResult<>(null, 400, "No password was given to create the doctor");
        if (doctorRepresentation.getRole() == null || !doctorRepresentation.getRole().equals("patient"))
            return new ApiResult<>(null, 400, "Incorrect role was passed in the doctor creation");
        if (usernameExistsInCredentials(doctorRepresentation.getUsername()))
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


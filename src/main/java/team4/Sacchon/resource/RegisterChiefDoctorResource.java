package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.ChiefDoctor;
import team4.Sacchon.model.Credentials;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.repository.CredentialsRepository;
import team4.Sacchon.representation.ChiefDoctorRepresentation;
import team4.Sacchon.representation.PatientRepresentation;

import javax.persistence.EntityManager;

import static team4.Sacchon.resource.ResourceUtils.usernameExistsInCredentials;

public class RegisterChiefDoctorResource extends ServerResource {

    @Post("json")
    public ApiResult<ChiefDoctorRepresentation> registerChiefDoctor(ChiefDoctorRepresentation chiefDoctorRepresentation) {
        ApiResult<ChiefDoctorRepresentation> checkedChiefDoctor = checkChiefDoctorInformation(chiefDoctorRepresentation);
        if (checkedChiefDoctor != null)
            return checkedChiefDoctor;

        ChiefDoctor chiefDoctor = createChiefDoctorFromRepresantation(chiefDoctorRepresentation);
        if (chiefDoctor != null) {
            return new ApiResult<>(new ChiefDoctorRepresentation(chiefDoctor), 200, "The Chief Doctor was successfully created");
        }
        return new ApiResult<>(null, 400, "The Chief Doctor could not be created.");
    }

    private ChiefDoctor createChiefDoctorFromRepresantation(ChiefDoctorRepresentation chiefDoctorRepresentation) {
        try {
            EntityManager em = JpaUtil.getEntityManager();
            ChiefDoctorRepository doctorRepository = new ChiefDoctorRepository(em);
            ChiefDoctor chiefDoctor = chiefDoctorRepresentation.createChiefDoctor();
            doctorRepository.save(chiefDoctor);
            new CredentialsRepository(em).save(new Credentials(chiefDoctor.getUsername()));
            em.close();
            return chiefDoctor;
        } catch (Exception e) {
            return null;
        }
    }

    private ApiResult<ChiefDoctorRepresentation> checkChiefDoctorInformation(ChiefDoctorRepresentation chiefDoctorRepresentation) {
        if (chiefDoctorRepresentation == null)
            return new ApiResult<>(null, 400, "No input data were given to create the Chief Doctor");
        if (chiefDoctorRepresentation.getName() == null)
            return new ApiResult<>(null, 400, "No name was given to create the Chief Doctor");
        if (chiefDoctorRepresentation.getUsername() == null)
            return new ApiResult<>(null, 400, "No username was given to create the Chief Doctor");
        if (chiefDoctorRepresentation.getPassword() == null)
            return new ApiResult<>(null, 400, "No password was given to create the Chief Doctor");
        if (chiefDoctorRepresentation.getRole() == null || !chiefDoctorRepresentation.getRole().equals("chief"))
            return new ApiResult<>(null, 400, "Incorrect role was passed in the Chief Doctor creation");
        if (usernameExistsInCredentials(chiefDoctorRepresentation.getUsername()))
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

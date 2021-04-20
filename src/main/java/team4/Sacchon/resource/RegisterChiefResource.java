package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.ChiefDoctor;
import team4.Sacchon.model.Credentials;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.repository.CredentialsRepository;
import team4.Sacchon.representation.ChiefDoctorRepresentation;

import javax.persistence.EntityManager;

public class RegisterChiefResource extends ServerResource {

    @Post("json")
    public ApiResult<ChiefDoctorRepresentation> registerPatient(ChiefDoctorRepresentation chiefDoctorRepresentation) {
        if (chiefDoctorRepresentation == null)
            return new ApiResult<>(null, 400, "No input data to create the chief doctor");
        if (chiefDoctorRepresentation.getName() == null)
            return new ApiResult<>(null, 400, "No name was given to create the chief doctor");
        if (chiefDoctorRepresentation.getUsername() == null)
            return new ApiResult<>(null, 400, "No username was given to create the chief doctor");
        if (usernameExists(chiefDoctorRepresentation.getUsername()))
            return new ApiResult<>(null, 400, "Duplicate username");

        ChiefDoctor chiefDoctor = chiefDoctorRepresentation.createChiefDoctor();
        EntityManager em = JpaUtil.getEntityManager();
        ChiefDoctorRepository chiefDoctorRepository = new ChiefDoctorRepository(em);
        chiefDoctorRepository.save(chiefDoctor);
        new CredentialsRepository(em).save(new Credentials(chiefDoctor.getUsername()));
        em.close();
        return new ApiResult<>(chiefDoctorRepresentation, 200, "The chief doctor was successfully created");
    }

    public boolean usernameExists(String candidateUsername) {
        EntityManager em = JpaUtil.getEntityManager();
        Credentials credentials;
        try {
            credentials = em.createQuery("SELECT c from Credentials c where c.username= :candidate", Credentials.class)
                    .setParameter("candidate", candidateUsername)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
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

package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.ChiefDoctor;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.representation.ChiefDoctorRepresentation;

import javax.persistence.EntityManager;

public class RegisterChiefDoctorResource extends ServerResource {
    @Post("json")
    public ApiResult<ChiefDoctorRepresentation> registerPatient(ChiefDoctorRepresentation chiefDoctorRepresentation) {
        if (chiefDoctorRepresentation == null)
            return new ApiResult<>(null, 400, "No input data to create the chiefDoctor");
        if (chiefDoctorRepresentation.getName() == null)
            return new ApiResult<>(null, 400, "No name was given to create the chiefDoctor");
        if (chiefDoctorRepresentation.getUsername() == null)
            return new ApiResult<>(null, 400, "No username was given to create the chiefDoctor");
        if (usernameExists(chiefDoctorRepresentation.getUsername()))
            return new ApiResult<>(null, 400, "Duplicate username");

        ChiefDoctor chiefDoctor = chiefDoctorRepresentation.createChiefDoctor();
        EntityManager em = JpaUtil.getEntityManager();
        ChiefDoctorRepository doctorRepository = new ChiefDoctorRepository(em);
        doctorRepository.save(chiefDoctor);
        return new ApiResult<>(chiefDoctorRepresentation, 200, "The chiefDoctor was successfully created");
    }

    public boolean usernameExists(String candidateUsername) {
        EntityManager em = JpaUtil.getEntityManager();
        ChiefDoctor chiefDoctor;
        try {
            chiefDoctor = em.createQuery("SELECT u from ChiefDoctor u where u.username= :candidate", ChiefDoctor.class)
                    .setParameter("candidate", candidateUsername)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return chiefDoctor != null;
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

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

public class RegisterDoctorResource extends ServerResource {

    @Post("json")
    public ApiResult<DoctorRepresentation> registerPatient(DoctorRepresentation doctorRepresentation) {
        if (doctorRepresentation == null)
            return new ApiResult<>(null, 400, "No input data to create the doctor");
        if (doctorRepresentation.getName() == null)
            return new ApiResult<>(null, 400, "No name was given to create the doctor");
        if (doctorRepresentation.getUsername() == null)
            return new ApiResult<>(null, 400, "No username was given to create the doctor");
        if (usernameExists(doctorRepresentation.getUsername()))
            return new ApiResult<>(null, 400, "Duplicate username");

        Doctor doctor = doctorRepresentation.createDoctor();
        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        doctorRepository.save(doctor);
        new CredentialsRepository(em).save(new Credentials(doctor.getUsername(), doctor.getPassword()));
        em.close();
        return new ApiResult<>(doctorRepresentation, 200, "The doctor was successfully created");
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

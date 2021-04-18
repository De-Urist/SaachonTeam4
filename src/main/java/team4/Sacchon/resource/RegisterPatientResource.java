package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.PatientRepresentation;

import javax.persistence.EntityManager;

public class RegisterPatientResource extends ServerResource {

    @Post("json")
    public ApiResult<PatientRepresentation> registerPatient(PatientRepresentation patientRepresentation) {
        if (patientRepresentation == null)
            return new ApiResult<>(null, 400, "No input data to create the customer");
        if (patientRepresentation.getName() == null)
            return new ApiResult<>(null, 400, "No name was given to create the customer");
        if (patientRepresentation.getUsername() == null)
            return new ApiResult<>(null, 400, "No username was given to create the customer");
        if (usernameExists(patientRepresentation.getUsername()))
            return new ApiResult<>(null, 400, "Duplicate username");

        Patient patient = patientRepresentation.createPatient();
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        patientRepository.save(patient);
        return new ApiResult<>(patientRepresentation, 200, "The customer was successfully created");
    }

    public boolean usernameExists(String candidateUsername) {
        EntityManager em = JpaUtil.getEntityManager();
        Patient patient;
        try {
            patient = em.createQuery("SELECT u from Patient u where u.username= :candidate", Patient.class)
                    .setParameter("candidate", candidateUsername)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return patient != null;
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

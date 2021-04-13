package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.PatientRepresentation;

import javax.persistence.EntityManager;

public class PatientResource extends ServerResource {
    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Get("json")
    public PatientRepresentation getPatient() {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.read(id);

        PatientRepresentation patientRepresentation = new PatientRepresentation(patient);
        em.close();
        return patientRepresentation;
    }
}

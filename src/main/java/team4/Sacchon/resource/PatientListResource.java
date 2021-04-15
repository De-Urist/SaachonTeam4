package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.PatientRepresentation;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PatientListResource extends ServerResource {

    @Get("json")
    public List<PatientRepresentation> getPatient(){
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository productRepository = new PatientRepository(em);
        List<Patient> patients = productRepository.findAll();
        em.close();

        return patients.stream()
        .map(PatientRepresentation::new)
        .collect(Collectors.toList());
    }
}

package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.repository.MeasurementRepository;
import team4.Sacchon.representation.MeasurementRepresentation;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class MeasurementListResource extends ServerResource {
    @Get("json")
    public List<MeasurementRepresentation> getRepresentation(){
        EntityManager em = JpaUtil.getEntityManager();
        MeasurementRepository measurementRepository = new MeasurementRepository(em);
        List<Measurement> consultation = measurementRepository.findAll();
        em.close();
        List<MeasurementRepresentation> measurementRepresentationList = new ArrayList<>();
        for(Measurement c : consultation){
            measurementRepresentationList.add(new MeasurementRepresentation(c));
        }
        return measurementRepresentationList;
    }
}

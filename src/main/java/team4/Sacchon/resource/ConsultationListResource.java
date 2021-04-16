package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Consultation;
import team4.Sacchon.repository.ConsultationRepository;
import team4.Sacchon.representation.ConsultationRepresentation;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class ConsultationListResource extends ServerResource {
    @Get("json")
    public List<ConsultationRepresentation> getConsultation(){
        EntityManager em = JpaUtil.getEntityManager();
        ConsultationRepository consultationRepository = new ConsultationRepository(em);
        List<Consultation> consultation = consultationRepository.findAll();
        em.close();
        List<ConsultationRepresentation> consultationRepresentationList = new ArrayList<>();
        for(Consultation c : consultation){
            consultationRepresentationList.add(new ConsultationRepresentation(c));
        }
        return consultationRepresentationList;
    }

    @Post("json")
    public ConsultationRepresentation add(ConsultationRepresentation consultationRepresentation){
        if(consultationRepresentation == null){
            return null;
        }
        if(consultationRepresentation.getPrescriptionName() == null || consultationRepresentation.getDosage() == 0){
            return null;
        }
        Consultation c = consultationRepresentation.createConsultation();
        EntityManager em = JpaUtil.getEntityManager();
        ConsultationRepository consultationRepository = new ConsultationRepository(em);
        consultationRepository.save(c);
        ConsultationRepresentation cr = new ConsultationRepresentation(c);
        return cr;
    }
}

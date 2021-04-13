package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Consultation;
import team4.Sacchon.repository.ConsultationRepository;
import team4.Sacchon.representation.ConsultationRepresentation;


import javax.persistence.EntityManager;

public class ConsultationResource extends ServerResource {
    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Get("json")
    public ConsultationRepresentation getConsultation() {
        EntityManager em = JpaUtil.getEntityManager();
        ConsultationRepository consultationRepository = new ConsultationRepository(em);
        Consultation consultation = consultationRepository.read(id);

        ConsultationRepresentation consultationRepresentation = new ConsultationRepresentation(consultation);
        em.close();
        return consultationRepresentation;
    }
}

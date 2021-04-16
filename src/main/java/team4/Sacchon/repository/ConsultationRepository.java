package team4.Sacchon.repository;

import team4.Sacchon.model.Consultation;

import javax.persistence.EntityManager;


public class ConsultationRepository extends Repository <Consultation, Integer>{

    public ConsultationRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<Consultation> getEntityClass() {
        return Consultation.class;
    }

    @Override
    public String getClassName() {
        return Consultation.class.getName();
    }


}

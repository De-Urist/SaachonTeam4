package team4.Sacchon.repository;

import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Measurement;

import javax.persistence.EntityManager;


public class ConsultationRepository extends Repository <Consultation, Integer>{

    private EntityManager em;
    public ConsultationRepository(EntityManager em) {
        super(em);
        this.em = em;
    }

    @Override
    public Class<Consultation> getEntityClass() {
        return Consultation.class;
    }

    @Override
    public String getClassName() {
        return Consultation.class.getName();
    }

    public Consultation getByPatientId(int patientId){
        return em.createQuery("from Consultation c WHERE c.patient.id= :patientId ", Consultation.class)
                .setParameter("patientId", patientId)
                .getSingleResult();
    }

    public Consultation getByDoctorId(int doctorId){
        return em.createQuery("from Consultation c WHERE c.doctorId.id= :doctorId ", Consultation.class)
                .setParameter("doctorId", doctorId)
                .getSingleResult();
    }

    public Consultation getByBoth(int doctorId, int patientId){
        return em.createQuery("from Consultation c WHERE c.doctorId.id= :doctorId AND c.patient.id= :patientId", Consultation.class)
                .setParameter("doctorId", doctorId)
                .setParameter("patientId", patientId)
                .getSingleResult();
    }

}

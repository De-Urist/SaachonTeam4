package team4.Sacchon.repository;

import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Measurement;

import javax.persistence.EntityManager;
import java.util.List;

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
        return em.createQuery("SELECT c FROM Patient p inner join p.consultations c WHERE p.id = :patientId ", Consultation.class)
                .setParameter("patientId", patientId)
                .getSingleResult();
    }

    public List<Consultation> getByDoctorId(int doctorId){
        return em.createQuery("SELECT c FROM Doctor d inner join d.consultations c WHERE d.id = :doctorId" , Consultation.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }

    public Consultation getByBoth(int doctorId, int patientId){
        return em.createQuery("SELECT c FROM Patient p inner join p.consultations c WHERE cp.id = :patientId AND Doctor d inner join d.consultations c WHERE d.id = :doctorId", Consultation.class)
                .setParameter("doctorId", doctorId)
                .setParameter("patientId", patientId)
                .getSingleResult();
    }

}

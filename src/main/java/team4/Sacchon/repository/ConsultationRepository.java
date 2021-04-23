package team4.Sacchon.repository;

import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.model.Patient;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

    public List<Consultation> getByPatientId(int patientId){
        return em.createQuery("SELECT c FROM Consultation c WHERE c.patient.id = :patientId ORDER BY c.creationDate DESC ", Consultation.class)
                .setParameter("patientId", patientId)
                .getResultList();
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

    public List<Patient> getPatientsWithConsultations() {
        Date currentDate = new Date();
        LocalDate oldDateLocal = LocalDate.now();
        LocalDate thirtyDaysAgo = oldDateLocal.minusDays(30);
        Date oldDate = java.util.Date.from(thirtyDaysAgo.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        return em.createQuery("SELECT DISTINCT p FROM Consultation c INNER JOIN c.patient p WHERE c.creationDate BETWEEN :startDate AND :endDate", Patient.class)
                .setParameter("startDate", oldDate)
                .setParameter("endDate", currentDate)
//                .setParameter("doctorId", doctorId)
                .getResultList();
    }

}

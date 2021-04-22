package team4.Sacchon.repository;

import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.model.Patient;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientRepository extends Repository <Patient,Integer>{

    private EntityManager em;
    public PatientRepository(EntityManager em) {
        super(em);
        this.em = em;
    }

    @Override
    public Class<Patient> getEntityClass() {
        return Patient.class;
    }

    @Override
    public String getClassName() {
        return Patient.class.getName();
    }


    public Patient getByUsername(String username) {
        return em.createQuery("SELECT p from Patient p WHERE p.username = :username", Patient.class)
                .setParameter("username", username)
                .getResultList().stream().findFirst().orElse(null);
    }

    public Doctor getDoctor(String username){
        return em.createQuery("SELECT d FROM Patient p inner join p.doctor d WHERE p.username = :username", Doctor.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    public List<Measurement> getMeasurements(String username){
        return em.createQuery("SELECT m FROM Patient p inner join p.measurement m WHERE p.username = :username", Measurement.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Consultation> getConsultations(String username, Date fromDate, Date toDate){
        return em.createQuery("SELECT c FROM Patient p inner join p.consultation c WHERE p.username = :username AND c.creationDate BETWEEN :fromDate AND :toDate", Consultation.class)
                .setParameter("username", username)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .getResultList();
    }

    public boolean canBeAdvised(int patientId){
        List<Measurement> l1 = new ArrayList<>();
        l1 = em.createQuery("SELECT m FROM Measurement m WHERE m.patient.id = :patientId", Measurement.class)
                .setParameter("patientId", patientId)
                .getResultList();
        return l1.size() >= 3;
    }

    public Consultation getLastModifiedConsultationForPatient(int patientId) {
        return em.createQuery("SELECT c FROM Consultation c WHERE c.patient.id = :patientId ORDER BY c.lastModified DESC", Consultation.class)
                .setParameter("patientId", patientId)
                .getResultList().stream().findFirst().orElse(null);
    }
}

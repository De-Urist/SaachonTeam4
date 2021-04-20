package team4.Sacchon.repository;

import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.model.Patient;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class DoctorRepository extends Repository<Doctor, Integer> {

    private EntityManager em;

    public DoctorRepository(EntityManager em) {
        super(em);
        this.em = em;
    }

    @Override
    public Class getEntityClass() {
        return Doctor.class;
    }

    @Override
    public String getClassName() {
        return Doctor.class.getName();
    }

    public Doctor getByUsername(String username) {
        return em.createQuery("from Doctor p WHERE p.username= :username", Doctor.class)
                .setParameter("username", username)
                .getResultList().stream().findFirst().orElse(null);
    }

    public List<Patient> getDoctorsPatientsById(int doctorId) {
        return em.createQuery("SELECT p FROM Patient pu INNER JOIN pu.patients p WHERE pu.id = :doctorId", Patient.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }

    public List<Patient> getDoctorsPatientsByUsername(String username) {
        return em.createQuery("SELECT p FROM Patient pu INNER JOIN pu.patients p WHERE pu.username = :username", Patient.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Consultation> getDoctorsConsultations(int doctorId) {
        return em.createQuery("SELECT c FROM Consultation cu INNER JOIN cu.consultations c WHERE cu.id = :doctorId", Consultation.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }

    public List<Measurement> getPatientMeasurements(String username) {
        return em.createQuery("SELECT p FROM Patient pm INNER JOIN pm.measurements p WHERE pm.username = :username", Measurement.class)
                .setParameter("username", username)
                .getResultList();
    }

    //updates via Consultation ID (The idea is that the doctor searches for a patient's consultations first and then changes them via their unique ID)
    public void updateConsultation(int id, String medName, int dosage) {
        Date newDate = new Date();
        em.createQuery("UPDATE Consultation c SET c.dosage = :dosage, c.prescriptionName = :medName, c.lastModified = :newDate WHERE c.id = :id")
                .setParameter("id", id)
                .setParameter("medName", medName)
                .setParameter("dosage", dosage)
                .setParameter("newDate", newDate)
                .executeUpdate();
    }

    //(TO DO) find patients that have not had a consultation in the last month

}

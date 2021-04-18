package team4.Sacchon.repository;

import team4.Sacchon.model.*;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class ChiefDoctorRepository extends Repository<ChiefDoctor,Integer>{

    private EntityManager em;
    public ChiefDoctorRepository(EntityManager em) {
        super(em);
        this.em = em;
    }

    @Override
    public Class<ChiefDoctor> getEntityClass() {
        return ChiefDoctor.class;
    }

    @Override
    public String getClassName() {
        return ChiefDoctor.class.getName();
    }
    
    public ChiefDoctor getByUsername(String username){
        ChiefDoctor pt = em.createQuery("from ChiefDoctor p WHERE p.username= :username", ChiefDoctor.class)
                .setParameter("username", username)
                .getSingleResult();
        return pt;
    }

    public List<Doctor> getAllDoctors(){
        return em.createQuery("SELECT d FROM Doctor d",Doctor.class).getResultList();
    }

    public List<Patient> getAllPatients(){
        return em.createQuery("SELECT p FROM Patient p",Patient.class).getResultList();
    }

    //add measurement over time range (TO DO)
    public List<Measurement> getMeasurementsByPatientName(String patientName){
        return em.createQuery("SELECT m FROM Measurement mu inner join mu.patients m WHERE mu.username = :patientName",Measurement.class)
                .setParameter("patientName", patientName)
                .getResultList();
    }

    //add consultations over a time range (TO DO)
    public List<Consultation> getConsultationsByDoctorName(String doctorName){
        return em.createQuery("SELECT c FROM Consultation cu inner join cu.doctors c WHERE cu.username = :doctorName",Consultation.class)
                .setParameter("doctorName", doctorName)
                .getResultList();
    }

    //In other words, without a doctor assigned to them.
    public List<Patient> getPatientsWithoutConsultation(){
        return em.createQuery("",Patient.class)
                .getResultList();
    }

    //Needs a timestamp last time a patient had a doctor assigned to them?
    public List<Patient> getPatientsLastConsultation(){
        return em.createQuery("",Patient.class)
                .getResultList();
    }

    //Returns a list of patients without measurements between startDate - endDate (TO DO)
    public List<Patient> getInactivePatients(Date startDate, Date endDate){
        return em.createQuery("SELECT p FROM Patient pm inner join pm.date p WHERE cu.username = :doctorName",Patient.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    //Similarly as above but with consultations.lastModified (TO DO)
    public List<Doctor> getInactiveDoctors(Date startDate, Date endDate){
        return em.createQuery("",Doctor.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}

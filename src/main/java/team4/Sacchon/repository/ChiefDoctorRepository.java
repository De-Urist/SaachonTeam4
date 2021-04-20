package team4.Sacchon.repository;

import team4.Sacchon.model.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public ChiefDoctor getByUsername(String username) {
        return em.createQuery("SELECT p from ChiefDoctor p WHERE p.username= :username", ChiefDoctor.class)
                .setParameter("username", username)
                .getResultList().stream().findFirst().orElse(null);
    }

    public List<Doctor> getAllDoctors(){
        return em.createQuery("SELECT * FROM Doctor",Doctor.class).getResultList();
    }

    public List<Patient> getAllPatients(){
        return em.createQuery("SELECT p FROM Patient p",Patient.class).getResultList();
    }
    //Numbering corresponds to the 4.4 Reporter section on the project specification (Page 6)
    //1
    public List<Measurement> getMeasurementsByPatientNameAndDates(String patientName, Date startDate, Date endDate){
        return em.createQuery("SELECT m FROM Measurement mu inner join mu.patients m WHERE mu.username = :patientName AND mu.date BETWEEN :startDate AND :endDate",Measurement.class)
                .setParameter("patientName", patientName)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    //2
    public List<Consultation> getConsultationsByDoctorNameAndDates(String doctorName, Date startDate, Date endDate){
        return em.createQuery("SELECT c FROM Consultation cu inner join cu.doctors c WHERE cu.username = :doctorName AND cu.creationDate BETWEEN :startDate AND :endDate",Consultation.class)
                .setParameter("doctorName", doctorName)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    //3
    public List<Patient> getPatientsWithoutDoctor(){
        return em.createQuery("SELECT p FROM Patient WHERE p.doctor = null",Patient.class)
                .getResultList();
    }

    public List<Patient> getPatientsWithoutConsultation(){
        return em.createQuery("SELECT p FROM Patient WHERE p.consultation = null",Patient.class)
                .getResultList();
    }

    //call getPatientsWithoutConsultation get list1 once
    //then for each Patient in list1, call the method below
    public Consultation getPatientsLastConsultation(String patientName){
        List <Consultation> allConsultations = em.createQuery("SELECT c FROM Consultation cp inner join cp.patients p WHERE cp.username = :patientName ORDER BY pc.consultation.creationDate DESC",Consultation.class)
                .setParameter("patientName", patientName)
                .getResultList();
        return  allConsultations.get(0); //DESC ordering, thus the first consultation is the newest (BY CREATION DATE NOT UPDATE DATE)
    }

    //4
    //Returns a list of patients without measurements between startDate - endDate
    public List<Patient> getInactivePatients(Date startDate, Date endDate){
        return em.createQuery("SELECT p FROM Patient pm inner join pm.measurement p WHERE pm.measurement.date NOT BETWEEN :startDate AND :endDate",Patient.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    //5
    //Similarly as above but with consultations.lastModified
    public List<Doctor> getInactiveDoctors(Date startDate, Date endDate){
        return em.createQuery("SELECT d FROM Doctor dc inner join dc.consultation d WHERE dc.consultation.lastModified NOT BETWEEN :startDate AND :endDate",Doctor.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}

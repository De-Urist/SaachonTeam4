package team4.Sacchon.repository;

import team4.Sacchon.model.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
        return em.createQuery("SELECT d FROM Doctor d",Doctor.class).getResultList();
    }

    public List<Patient> getAllPatients(){
        return em.createQuery("SELECT p FROM Patient p",Patient.class).getResultList();
    }
    //Numbering corresponds to the 4.4 Reporter section on the project specification (Page 6)
    //1
    public List<Measurement> getMeasurementsByPatientNameAndDates(String patientName, Date startDate, Date endDate){
        return em.createQuery("SELECT m FROM Patient p inner join p.measurement m WHERE p.username = :patientName AND m.date BETWEEN :startDate AND :endDate",Measurement.class)
                .setParameter("patientName", patientName)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    //2
    public List<Consultation> getConsultationsByDoctorIdAndDates(int doctorId, Date startDate, Date endDate){
        return em.createQuery("SELECT c FROM Consultation c WHERE c.doctor.id = :doctorId AND c.creationDate BETWEEN :startDate AND :endDate",Consultation.class)
                .setParameter("doctorId", doctorId)
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
    public Consultation getPatientLastConsultation(int patientId){
        List <Consultation> allConsultations = em.createQuery("SELECT c FROM Consultation c WHERE c.patient.id = :patientId ORDER BY c.creationDate DESC",Consultation.class)
                .setParameter("patientId", patientId)
                .getResultList();
        if (allConsultations.size() != 0)
            return  allConsultations.get(0); //DESC ordering, thus the first consultation is the newest (BY CREATION DATE NOT UPDATE DATE)
        return null;
    }

    public List getPatientAwaitingConsultation() {
        Query q = em.createNativeQuery("select patient.* from patient where not patient.id in (select distinct patient_id from consultation where consultation.creationDate between dateadd(day, -30, getdate()) and getdate() )");
        return q.getResultList();
    }

    //4
    //Returns a list of patients without measurements between startDate - endDate
    public List<Patient> getInactivePatients(Date startDate, Date endDate){
        return em.createQuery("SELECT DISTINCT p FROM Measurement m inner join m.patient p WHERE m.date BETWEEN :startDate AND :endDate",Patient.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    //5
    //Similarly as above but with consultations.lastModified
    public List<Doctor> getInactiveDoctors(Date startDate, Date endDate){
        return em.createQuery("SELECT DISTINCT d FROM Consultation c inner join c.doctor d WHERE c.creationDate BETWEEN :startDate AND :endDate AND d.username is not null",Doctor.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}

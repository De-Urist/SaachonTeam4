package team4.Sacchon.repository;

import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.model.Patient;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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


    public Patient getByUsername(String username){
        try{
            return em.createQuery("SELECT p from Patient p WHERE p.username = :username", Patient.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e){
            return null;
        }
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

    public void updateInformation(int id, String name, String username, String password) {
        em.createQuery("UPDATE Patient p SET p.name = :name WHERE p.id = :id")
                .setParameter("id", id)
                .setParameter("name", name)
                .executeUpdate();
//                .setParameter("username", username)
//                .setParameter("password", password);
    }

    //Make it via Date range (TO DO)
    public List<Consultation> getConsultations(String username){
        return em.createQuery("SELECT c FROM Patient p inner join p.consultation c WHERE p.username = :username", Consultation.class)
                .setParameter("username", username)
                .getResultList();
    }

    //Make it via Date range (TO DO)
    public Double getAverageGlucoseLevel(String username){
        return (Double) em.createQuery("SELECT avg(glucoseLevel) FROM Patient p inner join p.measurement avg(glucoseLevel) WHERE p.username = :username ")
                .setParameter("username", username)
                .getSingleResult();
    }

    public Double getAverageCarbIntake(String username){
        return (Double) em.createQuery("SELECT avg(carbIntake) FROM Patient p inner join p.measurement avg(carbIntake) WHERE p.username = :username ")
                .setParameter("username", username)
                .getSingleResult();
    }
}

package team4.Sacchon.repository;

import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Patient;

import javax.persistence.EntityManager;
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
        return em.createQuery("SELECT p from Patient p WHERE p.username = :username", Patient.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}

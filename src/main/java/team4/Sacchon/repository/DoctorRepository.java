package team4.Sacchon.repository;

import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Patient;

import javax.persistence.EntityManager;
import java.util.List;

public class DoctorRepository extends Repository <Doctor,Integer> {

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

    public Doctor getByUsername(String username){
        Doctor pt = em.createQuery("from Doctor p WHERE p.username= :username", Doctor.class)
                .setParameter("username", username)
                .getSingleResult();
        return pt;
    }

    public List<Patient> getPatients(int doctorId){
        return em.createQuery("SELECT p FROM Patient pu INNER JOIN pu.patients p WHERE pu.id = :doctorId", Patient.class)
                 .setParameter("doctorId", doctorId)
                 .getResultList();
    }
}

package team4.Sacchon.repository;

import team4.Sacchon.model.ChiefDoctor;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Patient;

import javax.persistence.EntityManager;
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
}

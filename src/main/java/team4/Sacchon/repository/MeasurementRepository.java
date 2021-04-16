package team4.Sacchon.repository;

import team4.Sacchon.model.Measurement;
import team4.Sacchon.model.Patient;

import javax.persistence.EntityManager;
import java.util.List;

public class MeasurementRepository extends Repository<Measurement,Integer>{

    private EntityManager em;
    public MeasurementRepository(EntityManager em) {
        super(em);
        this.em = em;
    }

    @Override
    public Class<Measurement> getEntityClass() {
        return Measurement.class;
    }

    @Override
    public String getClassName() {
        return Measurement.class.getName();
    }

    public Measurement getById(int id){
        return em.createQuery("SELECT m from Measurement m WHERE m.id = :id", Measurement.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    //TESTING m.patientId.id -> ?Output
    public int countMeasurements(int patientId){
        List <Measurement> l1 = em.createQuery("SELECT count FROM Measurement m WHERE m.patientId.id = :patientId", Measurement.class)
                .setParameter("patientId", patientId)
                .getResultList();
        return l1.size();
    }
}

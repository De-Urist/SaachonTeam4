package team4.Sacchon.repository;

import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.model.Patient;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

public class MeasurementRepository extends Repository<Measurement, Integer> {

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

    public Measurement getById(int id) {
        try {
            return em.createQuery("SELECT m from Measurement m WHERE m.id = :id", Measurement.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Measurement> getMeasurementsOf(int id) {
        return em.createQuery("SELECT m from Measurement m WHERE m.patient.id = :id", Measurement.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Measurement> getMeasurementsOfIdBetween(int id, Date fromDate, Date toDate) {
        return em.createQuery("SELECT m from Measurement m WHERE m.patient.id = :id AND m.date BETWEEN :fromDate AND :toDate", Measurement.class)
                .setParameter("id", id)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .getResultList();
    }

    public double getAverageGlucoseOfMeasurements(int id, Date fromDate, Date toDate) {
        try {
            return em.createQuery("SELECT AVG (m.glucoseLevel) from Measurement m WHERE m.patient.id = :id AND m.date BETWEEN :fromDate AND :toDate", Double.class)
                    .setParameter("id", id)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .getSingleResult();
        } catch (Exception e) {
            return 0;
        }

    }

    public double getAverageCarbOfMeasurements(int id, Date fromDate, Date toDate) {
        try {
            return em.createQuery("SELECT AVG (m.carbIntake) from Measurement m WHERE m.patient.id = :id AND m.date BETWEEN :fromDate AND :toDate", Double.class)
                    .setParameter("id", id)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    //TESTING m.patientId.id -> ?Output
    public int countMeasurements(int patientId) {
        List<Measurement> l1 = em.createQuery("SELECT count FROM Measurement m WHERE m.patientId.id = :patientId", Measurement.class)
                .setParameter("patientId", patientId)
                .getResultList();
        return l1.size();
    }

    public Measurement getPatientFirstMeasurement(int patientId){
        List <Measurement> measurements = em.createQuery("SELECT m FROM Measurement m WHERE m.patient.id = :patientId ORDER BY m.date ASC ",Measurement.class)
                .setParameter("patientId", patientId)
                .getResultList();
        if (measurements.size() != 0)
            return  measurements.get(0);
        return null;
    }
}

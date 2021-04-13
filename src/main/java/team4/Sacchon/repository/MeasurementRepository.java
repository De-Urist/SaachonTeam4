package team4.Sacchon.repository;

import team4.Sacchon.model.Measurement;

import javax.persistence.EntityManager;

public class MeasurementRepository extends Repository<Measurement,Integer>{

    public MeasurementRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<Measurement> getEntityClass() {
        return Measurement.class;
    }

    @Override
    public String getClassName() {
        return Measurement.class.getName();
    }
}

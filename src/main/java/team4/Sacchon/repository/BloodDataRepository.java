package team4.Sacchon.repository;

import team4.Sacchon.model.BloodData;

import javax.persistence.EntityManager;

public class BloodDataRepository extends Repository<BloodData,Integer>{

    public BloodDataRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<BloodData> getEntityClass() {
        return BloodData.class;
    }

    @Override
    public String getClassName() {
        return BloodData.class.getName();
    }
}

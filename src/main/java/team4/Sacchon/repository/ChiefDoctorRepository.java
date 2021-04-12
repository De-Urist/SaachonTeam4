package team4.Sacchon.repository;

import team4.Sacchon.model.ChiefDoctor;

import javax.persistence.EntityManager;

public class ChiefDoctorRepository extends Repository<ChiefDoctor,Integer>{

    public ChiefDoctorRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<ChiefDoctor> getEntityClass() {
        return ChiefDoctor.class;
    }

    @Override
    public String getClassName() {
        return ChiefDoctor.class.getName();
    }

}

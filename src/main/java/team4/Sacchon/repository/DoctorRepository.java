package team4.Sacchon.repository;

import team4.Sacchon.model.Doctor;

import javax.persistence.EntityManager;

public class DoctorRepository extends Repository <Doctor,Integer> {

    public DoctorRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class getEntityClass() {
        return Doctor.class;
    }

    @Override
    public String getClassName() {
        return Doctor.class.getName();
    }
}

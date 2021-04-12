package team4.Sacchon.repository;

import team4.Sacchon.model.Patient;

import javax.persistence.EntityManager;

public class PatientRepository extends Repository <Patient,Integer>{

    public PatientRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<Patient> getEntityClass() {
        return Patient.class;
    }

    @Override
    public String getClassName() {
        return Patient.class.getName();
    }
}

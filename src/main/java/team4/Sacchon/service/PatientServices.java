package team4.Sacchon.service;

import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.PatientRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class PatientServices {
    //createPatient should get data (name,username...) via front end?
    public static void createPatient(EntityManager em, String name, String username, String password){
        PatientRepository pr = new PatientRepository(em);
        System.out.println("Patient added to the system with variables : " + name + ", " + username);
    }

    public static List searchAllPatients(EntityManager em){
        PatientRepository pr = new PatientRepository(em);
        List<Patient> l1 = pr.findAll();
        return l1;
    }

}

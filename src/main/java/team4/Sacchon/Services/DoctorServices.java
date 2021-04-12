package team4.Sacchon.Services;

import team4.Sacchon.model.Doctor;
import team4.Sacchon.repository.DoctorRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class DoctorServices {
    public static void createDoctor(EntityManager em, String name, String username, String password){
        DoctorRepository pr = new DoctorRepository(em);
        System.out.println("Doctor added to the system with variables : " + name + ", " + username);
    }

    public static List searchAllDoctors(EntityManager em){
        DoctorRepository dr = new DoctorRepository(em);
        List<Doctor> l1 = dr.findAll();
        return l1;
    }

}


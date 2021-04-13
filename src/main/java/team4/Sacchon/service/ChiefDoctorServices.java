package team4.Sacchon.service;

import team4.Sacchon.model.ChiefDoctor;
import team4.Sacchon.repository.ChiefDoctorRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class ChiefDoctorServices {
    public static void createChiefDoctor(EntityManager em, String name, String username, String password){
        ChiefDoctorRepository pr = new ChiefDoctorRepository(em);
        System.out.println("Chief doctor added to the system with variables : " + name + ", " + username);
    }

    public static List searchAllDoctors(EntityManager em){
        ChiefDoctorRepository cdr = new ChiefDoctorRepository(em);
        List<ChiefDoctor> l1 = cdr.findAll();
        return l1;
    }

}

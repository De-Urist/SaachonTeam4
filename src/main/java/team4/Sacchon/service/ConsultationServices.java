package team4.Sacchon.service;

import team4.Sacchon.model.Consultation;
import team4.Sacchon.repository.ConsultationRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class ConsultationServices {
    public static void createConsultation(EntityManager em, String name, int dosage, String prescriptionName){
        ConsultationRepository pr = new ConsultationRepository(em);
        System.out.println("Consultation added to the system with prescription name: " + prescriptionName + ",dosage : " + dosage);
    }

    public static List searchAllConsultations(EntityManager em){
        ConsultationRepository cr = new ConsultationRepository(em);
        List<Consultation> l1 = cr.findAll();
        return l1;
    }
}

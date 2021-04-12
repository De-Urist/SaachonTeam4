package team4.Sacchon.Services;

import team4.Sacchon.model.BloodData;
import team4.Sacchon.repository.BloodDataRepository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class BloodDataServices {
    public static void createBloodData(EntityManager em, Date date, int glucoseLevel, int carbIntake){
        BloodDataRepository pr = new BloodDataRepository(em);
        System.out.println("Blood data added to the system at: " + date.toString() + " with variables : " + glucoseLevel + "mg/dL, " + carbIntake + "g");
    }

    public static List searchAllBloodData(EntityManager em){
        BloodDataRepository bdr = new BloodDataRepository(em);
        List<BloodData> l1 = bdr.findAll();
        return l1;
    }
}

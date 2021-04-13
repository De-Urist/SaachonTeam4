package team4.Sacchon.service;

import team4.Sacchon.model.Measurement;
import team4.Sacchon.repository.MeasurementRepository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class MeasurementServices {
    public static void createMeasurement(EntityManager em, Date date, int glucoseLevel, int carbIntake){
        MeasurementRepository pr = new MeasurementRepository(em);
        System.out.println("Blood data added to the system at: " + date.toString() + " with variables : " + glucoseLevel + "mg/dL, " + carbIntake + "g");
    }

    public static List searchAllMeasurements(EntityManager em){
        MeasurementRepository bdr = new MeasurementRepository(em);
        List<Measurement> l1 = bdr.findAll();
        return l1;
    }
}

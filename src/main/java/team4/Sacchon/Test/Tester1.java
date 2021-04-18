package team4.Sacchon.Test;

import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.ConsultationRepository;
import team4.Sacchon.repository.DoctorRepository;
import team4.Sacchon.repository.PatientRepository;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tester1 {
    public static void testMe1(EntityManager em){

    }

    public static void createSingularData(EntityManager em){

        //doctor
        DoctorRepository dr = new DoctorRepository(em);
        Doctor d = new Doctor();
        d.setName("Dr.Kev");
        d.setPassword("123");
        d.setRole("DOCTOR");
        dr.save(d);

        //patient
        PatientRepository pr = new PatientRepository(em);
        Patient p = new Patient();
        p.setName("John");
        p.setPassword("123");
        p.setDoctor(d);
        p.setRole("PATIENT");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        pr.save(p);

        //consultations
        ConsultationRepository cr = new ConsultationRepository(em);
        Consultation c = new Consultation();
        c.setPrescriptionName("Med1");
        c.setDosage(5);
        c.setDoctor(d);
        c.setPatient(p);
        c.setCreationDate(new Date());
        cr.save(c);
    }
}

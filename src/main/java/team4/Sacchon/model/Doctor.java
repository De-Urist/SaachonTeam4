package team4.Sacchon.model;

import jdk.internal.jimage.ImageStrings;
import lombok.Data;
import org.restlet.engine.application.ApplicationHelper;

import javax.persistence.*;
import java.util.Date;
import java.util.List;




@Data
@Entity
@DiscriminatorValue("doctor")
public class Doctor extends ApplicationHelper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String lastName;
    private String firstName;
    private String name;
    private String username;
    private String password;


    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Patient> patients;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consultation> consultations;

    public void consultPatient(Patient patient, Consultation consultation)
    {
        consultation.setCreationDate(new Date());
        consultation.setDoctorName(firstName + " " + lastName);
        patient.addConsultation(consultation);
        patient.setDoctor(this);
        listOfPatients.add(patient);
    }


}

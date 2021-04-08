package Objects;

import java.util.ArrayList;
import java.util.List;

public class Doctor {
    private int id;
    private String name = "";
    private String email = "";
    private List <Patient> patientsList = new ArrayList<>();
    private List <Consultation> consultationsList = new ArrayList<>();

    //double constructor?
    public Doctor(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    //getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public List<Patient> getPatientsList() {
        return patientsList;
    }
    public List<Consultation> getConsultationsList() {
        return consultationsList;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    //other methods

}

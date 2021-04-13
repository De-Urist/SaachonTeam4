package team4.Sacchon.representation;

import lombok.Data;
import lombok.NoArgsConstructor;
import team4.Sacchon.model.Patient;

import java.util.Date;
@Data
@NoArgsConstructor
public class PatientRepresentation {
    private int id;

    private String name;
    private String username;
    private String password;
    private Date lastLogin;

    private String uri;

    public PatientRepresentation(Patient patient){
        if(patient != null){
            name = patient.getName();
            username = patient.getUsername();
            password = patient.getPassword();

            uri = "http://localhost:9000/v1/patient/" + patient.getId();
        }
    }
    public Patient createPatient(){
        Patient patient = new Patient();
        patient.setName(name);
        patient.setUsername(username);
        patient.setPassword(password);
        return patient;
    }
}

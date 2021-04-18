package team4.Sacchon.representation;

import lombok.Data;
import lombok.NoArgsConstructor;
import team4.Sacchon.model.Doctor;

@Data
@NoArgsConstructor
public class DoctorRepresentation {

    private int id;
    private String name;
    private String username;
    private String password;
    private String role;
    private String uri;

    public DoctorRepresentation(Doctor doctor) {
        if (doctor != null) {
            name = doctor.getName();
            username = doctor.getUsername();
            password = doctor.getPassword();
            role = doctor.getRole();
            uri = "http://localhost:9000/v1/doctor/" + doctor.getId();
        }
    }

    public Doctor createDoctor() {
        Doctor doctor = new Doctor();
        doctor.setName(name);
        doctor.setUsername(username);
        doctor.setPassword(password);
        doctor.setRole(role);
        return doctor;
    }
}

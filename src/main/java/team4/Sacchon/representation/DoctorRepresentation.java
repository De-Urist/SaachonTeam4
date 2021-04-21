package team4.Sacchon.representation;

import lombok.Data;
import lombok.NoArgsConstructor;
import team4.Sacchon.model.Doctor;
import java.util.Date;

@Data
@NoArgsConstructor
public class DoctorRepresentation {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String uri;

    static public Doctor getDoctor(DoctorRepresentation doctorRepresentation) {
        Doctor doctor = new Doctor();

        doctor.setUsername(doctorRepresentation.getUsername());
        doctor.setFirstName(doctorRepresentation.getFirstName());
        doctor.setLastName(doctorRepresentation.getLastName());
        doctor.setEmail(doctorRepresentation.getEmail());
        doctor.setPassword(doctorRepresentation.getPassword());
        doctor.setAddress(doctorRepresentation.getAddress());

        return doctor;

    }


    static public DoctorRepresentation getDoctorRepresentation(Doctor doctor) {
        DoctorRepresentation doctorRepresentation = new DoctorRepresentation();

        doctorRepresentation.setId(doctor.getId());
        doctorRepresentation.setUsername(doctor.getUsername());
        doctorRepresentation.setFirstName(doctor.getFirstName());
        doctorRepresentation.setLastName(doctor.getLastName());
        doctorRepresentation.setEmail(doctor.getEmail());
        doctorRepresentation.setAddress(doctor.getAddress());


        doctorRepresentation.setUri("http://localhost:9000/SacchonApp/doctor/" + doctor.getId());

        return doctorRepresentation;
    }


}




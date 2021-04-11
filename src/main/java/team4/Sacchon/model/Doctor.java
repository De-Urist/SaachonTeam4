package team4.Sacchon.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Doctor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String username;
    private String password;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Patient> patients;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consultation> consultations;
}

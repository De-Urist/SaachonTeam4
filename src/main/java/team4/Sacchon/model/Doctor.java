package team4.Sacchon.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Doctor extends User{
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Patient> patients;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Consultation> consultations;
}

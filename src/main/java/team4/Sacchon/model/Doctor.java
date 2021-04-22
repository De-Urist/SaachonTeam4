package team4.Sacchon.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Doctor extends User{
    private Date lastLogin;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Patient> patients;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Consultation> consultations;

    @Override
    public String toString() {
        return "";
    }
}

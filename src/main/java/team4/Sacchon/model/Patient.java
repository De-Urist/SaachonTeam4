package team4.Sacchon.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Patient{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String username;
    private String password;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Doctor doctor;
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BloodData> bloodDataList;
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Consultation> consultationList;
}

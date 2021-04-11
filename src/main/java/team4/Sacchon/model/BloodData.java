package team4.Sacchon.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class BloodData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;
    private int glucoseLevel;
    private int carbIntake;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Patient patient;
}

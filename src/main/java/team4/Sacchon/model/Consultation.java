package team4.Sacchon.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int dosage;
    private String prescriptionName;
    //maybe add
    //creationDate when this consultation was created
    //lastModified when this consultation was last changed

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Doctor doctor;
}

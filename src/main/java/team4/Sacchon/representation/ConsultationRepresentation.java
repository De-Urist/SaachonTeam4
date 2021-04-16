package team4.Sacchon.representation;

import lombok.Data;
import lombok.NoArgsConstructor;
import team4.Sacchon.model.Consultation;

import java.util.Date;

@Data
@NoArgsConstructor
public class ConsultationRepresentation {
    private int id;

    private int dosage;
    private String prescriptionName;
    private Date creationDate;
    private Date lastModified;
    //foreign key id of doctor and patient

    private String uri;

    public ConsultationRepresentation(Consultation consultation){
        if(consultation != null){
            dosage = consultation.getDosage();
            prescriptionName = consultation.getPrescriptionName();
            creationDate = consultation.getCreationDate();
            lastModified = consultation.getLastModified();

            uri = "http://localhost:9000/v1/consultation/" + consultation.getId();
        }
    }

    public Consultation createConsultation(){
        Consultation consultation = new Consultation();
        consultation.setDosage(dosage);
        consultation.setPrescriptionName(prescriptionName);
        consultation.setCreationDate(creationDate);
        consultation.setLastModified(lastModified);
        return consultation;
    }
}

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
    private int doctorId;
    private int patientId;
    private String uri;

    public ConsultationRepresentation(Consultation consultation){
        if(consultation != null){
            dosage = consultation.getDosage();
            prescriptionName = consultation.getPrescriptionName();
            creationDate = consultation.getCreationDate();
            lastModified = consultation.getLastModified();
            if (consultation.getDoctor() != null)
                doctorId = consultation.getDoctor().getId();
            if (consultation.getPatient() != null)
                patientId = consultation.getPatient().getId();
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

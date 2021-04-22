package team4.Sacchon.representation;

import lombok.Data;
import lombok.NoArgsConstructor;
import team4.Sacchon.model.Consultation;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class ConsultationRepresentation {

    private int id;
    private int dosage;
    private String prescriptionName;
    private String creationDate;
    private String creationTime;
    private String modifiedDate;
    private String modifiedTime;
    private int doctorId;
    private int patientId;
    private String uri;

    public ConsultationRepresentation(Consultation consultation) {
        if (consultation != null) {
            id = consultation.getId();
            dosage = consultation.getDosage();
            prescriptionName = consultation.getPrescriptionName();
            creationDate = new SimpleDateFormat("dd/MM/yyyy").format(consultation.getCreationDate());
            creationTime = new SimpleDateFormat("HH:mm:ss").format(consultation.getCreationDate());
            if (consultation.getLastModified() != null) {
                modifiedDate = new SimpleDateFormat("dd/MM/yyyy").format(consultation.getLastModified());
                modifiedTime = new SimpleDateFormat("HH:mm:ss").format(consultation.getLastModified());
            }
            if (consultation.getDoctor() != null)
                doctorId = consultation.getDoctor().getId();
            if (consultation.getPatient() != null)
                patientId = consultation.getPatient().getId();
            uri = "http://localhost:9000/v1/consultation/" + consultation.getId();
        }
    }

    public Consultation createConsultation() throws Exception{
        Consultation consultation = new Consultation();
        consultation.setDosage(dosage);
        consultation.setPrescriptionName(prescriptionName);
        consultation.setCreationDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(creationDate + " " + creationTime));
        if (modifiedDate.length() != 0)
            consultation.setLastModified(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(modifiedDate + " " + modifiedTime));
        return consultation;
    }
}

package Objects;

import java.util.Date;

public class Consultation {
    private int id;
    private String medicineName = "";
    private int dosage;
    private Date creationDate;
    private Date lastModified;

    public Consultation(int id, String medicineName, int dosage, Date creationDate, Date lastModified) {
        this.id = id;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
    }

    public Consultation(){}


    //getters
    public int getId() {
        return id;
    }
    public String getMedicineName() {
        return medicineName;
    }
    public int getDosage() {
        return dosage;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public Date getLastModified() {
        return lastModified;
    }

    //setters
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
    public void setDosage(int dosage) {
        this.dosage = dosage;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}

package Objects;

import java.util.Date;

public class BloodData {
    private int id;
    private Date DataDate;
    private int glucoseLevel;
    private int carbIntake;
    private Patient belongsTo; //wrong logic?

    //similarly to patient, call once on creation
    public BloodData(int id, Date dataDate, int glucoseLevel, int carbIntake, Patient belongsTo) {
        this.id = id;
        DataDate = dataDate;
        this.glucoseLevel = glucoseLevel;
        this.carbIntake = carbIntake;
        this.belongsTo = belongsTo;
        //After this object is created, INSERT the data into
        //the BloodData table
    }

    //Get via queries to the database
    //Or manage to keep the object in memory on runtime
    //and do all calls via Java
    public int getId() {
        return id;
    }
    public Date getDataDate() {
        return DataDate;
    }
    public int getGlucoseLevel() {
        return glucoseLevel;
    }
    public int getCarbIntake() {
        return carbIntake;
    }
    public Patient getBelongsTo() {
        return belongsTo;
    }

    //Setters mainly for updating
    public void setDataDate(Date dataDate) {
        //updates time, to the current time
        DataDate = new java.util.Date();
    }
    public void setGlucoseLevel(int glucoseLevel) {
        //mathematical types for dynamic calculation?
        this.glucoseLevel = glucoseLevel;
    }
    public void setCarbIntake(int carbIntake) {
        this.carbIntake = carbIntake;
    }
    //Redundant?
    public void setBelongsTo(Patient belongsTo) {
        this.belongsTo = belongsTo;
    }
}

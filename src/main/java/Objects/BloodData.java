package Objects;

import java.util.Date;

public class BloodData {
    private int id;
    private Date dataDate;
    private Date dataTime;
    private int glucoseLevel;
    private int carbIntake;

    //similarly to patient, call once on creation
    public BloodData(int id, Date dataDate, Date dataTime, int glucoseLevel, int carbIntake) {
        this.id = id;
        this.dataDate = dataDate;
        this.dataTime = dataTime;
        this.glucoseLevel = glucoseLevel;
        this.carbIntake = carbIntake;
        //After this object is created, INSERT the data into
        //the BloodData table
    }

    public BloodData(){

    }

    //Get via queries to the database
    //Or manage to keep the object in memory on runtime
    //and do all calls via Java
    public int getId() {
        return id;
    }
    public Date getDataDate() {
        return dataDate;
    }
    public int getGlucoseLevel() {
        return glucoseLevel;
    }
    public int getCarbIntake() {
        return carbIntake;
    }
    public Date getDataTime() {
        return dataTime;
    }

    //toString
    @Override
    public String toString() {
        return "BloodData{" +
                "glucoseLevel=" + glucoseLevel + "mg/dL" +
                ", carbIntake=" + carbIntake + "g" +
                '}';
    }

    //Setters mainly for updating
    public void setDataDate(Date dataDate) {
        //updates time, to the current time
        dataDate = new java.util.Date();
    }
    public void setGlucoseLevel(int glucoseLevel) {
        //mathematical types for dynamic calculation?
        this.glucoseLevel = glucoseLevel;
    }
    public void setCarbIntake(int carbIntake) {
        this.carbIntake = carbIntake;
    }
    public void setDataTime(Date dataTime) {
        //updates data time
        this.dataTime = dataTime;
    }
}

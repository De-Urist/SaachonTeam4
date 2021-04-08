package Objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient {
    private int id;
    private String name = "";
    private String email = "";
    private Date lastOnline;
    private List<BloodData> dataList = new ArrayList<>();
    private List<Consultation> consultationList = new ArrayList<>();

    //constructor should be called once, in the first creation
    //of the patient's account. (Sign up)
    public Patient(int id, String name, String email, Date lastOnline) {
        //In here also do the queries in the db
        //table Patiens: INSERT (id,name,lastOnLine)_
        this.id = id;
        this.name = name;
        this.email = email;
        this.lastOnline = lastOnline;
    }
    //Idea. Double constructors.
    //One for first initialization INSERT -> db
    //Two for creation of a temp object by getting data from the db

    //Getters = Queries in the db and return locally / to rest sockets
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Date getLastOnline() {
        return lastOnline;
    }
    public String getNameOfManagingDoctor(){
        //search db for patient with name
        //find DoctorID (foreign key)
        //get name from doctor with DoctorID
        return "ABC";
    }
    //2 redundant methods?
    public List<BloodData> getDataList() {
        return dataList;
    }
    public List<Consultation> getConsultationList() {
        return consultationList;
    }

    //Setters = Update in db
    public void setName(String name) {
        this.name = name;
    }
    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }

    //Other methods
    public void deleteSelf(){
        //DELETE from table Patients with id
    }
    public void deleteData(BloodData data){
        //DELETE from table BloodData
        dataList.remove(data);
    }
}

package team4.Saachon.Objects;

import team4.Saachon.Services.BloodDataChecker;
import team4.Saachon.Services.ConsultationChecker;

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
    private BloodDataChecker bloodChecker = new BloodDataChecker();         //use this class to import methods
    private ConsultationChecker consultChecker = new ConsultationChecker(); //this one too

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
    public Patient(){

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
        //Update every time user logs in
        this.lastOnline = lastOnline;
    }

    //Other methods
    public void deleteSelf(){
        //DELETE from table Patients with id
        //also DELETE from table BloodData with id
    }
    public void deleteData(BloodData data){
        //DELETE from table BloodData
        dataList.remove(data);
    }

    //redundant
    public boolean hadLastMonthConsultation(int id){
        List<Consultation> lastMonthList = new ArrayList<>();
        if(lastMonthList.isEmpty()){
            return true; //had a consultation in the last month
        }
        else{
            return false;
        }
    }

}

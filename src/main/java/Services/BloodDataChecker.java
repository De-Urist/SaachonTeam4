package Services;

import Objects.BloodData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BloodDataChecker {
    public List addData(int patientID, List inputList, Date dataDate, Date dataTime, int glucoseLevel, int carbIntake){
        int dataID = 0; //Replace with method, returns meaningfull integer id (probably via database)
        BloodData newData = new BloodData(dataID,dataDate,dataTime,glucoseLevel,carbIntake);
        inputList.add(newData);
        return inputList;
    }

    public List checkAllData(int id){
        List<BloodData> output = new ArrayList<>();
        //search database with id and add each query to output list
        //more detailed: Query table BloodDate where PatientID = id
        return output;
    }

    public List checkGlucosePeriod(int id, Date oldDate, Date newDate){
        List<BloodData> output = new ArrayList<>();
        //Query table BloodData with PatientID = id and datespan (oldDate-newDate)
        return output;
    }

    public int averageGlucosePeriod(int id, Date oldDate, Date newDate){
        int average = 0;
        //perform query, find all glucose levels from oldDate - newDate
        //add them and average them in value average.

        return average;
    }

    public int averageCarbPeriod(int id, Date oldDate, Date newDate){
        int average = 0;
        //perform query, find all carb intake levels from oldDate - newDate
        //add them and average them in value average.

        return average;
    }


}

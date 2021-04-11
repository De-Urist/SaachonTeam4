package team4.Saachon.Services;

import team4.Saachon.Objects.Consultation;
import team4.Saachon.Objects.Patient;

import java.util.ArrayList;
import java.util.List;

//Should be divided with interface since we have different implementation for patient / doctor
public class ConsultationChecker {
    public List checkConsultation(int id){
        List<Consultation> output= new ArrayList<>();
        //similarly to BloodDataChecker (query table, add to output list)
        return output;
    }

    public List checkLastMonthConsultation(int id){
        List<Consultation> output= new ArrayList<>();
        //similarly to BloodDataChecker (query table, add to output list with -30days date)
        return output;
    }

    public List checkAllPatientsWithoutConsult(){
        List<Patient> output= new ArrayList<>();
        //add to list all
        return output;
    }

}

package team4.Sacchon.date;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateChecker {

    //This method will be used to find out if a patient did not
    //have a consultation the past 30 days
    //Accepts only LocalDate variables, requires conversion see below
    public boolean compareConsultationDates(LocalDate newDate, LocalDate oldDate){
        LocalDate newDateMinus30 = newDate.minusDays(30);
        if(newDateMinus30.isAfter(oldDate)){
            System.out.println("More than 30 days! ");
            return true;
        }
        else{
            System.out.println("Less than 30 days! ");
            return false;
        }
    }

    //find out if patient's last login date is before a consultation change
    //true = Patient has not seen the changes in the consultations modified
    //after the last time he logged in
    //false = Patient has seen this consultation last time he was logged in
    public boolean compareLoginDates(Date patientLastLoginDate, Date lastConsultationDate){
        if(patientLastLoginDate.compareTo(lastConsultationDate) > 0){
            return false;
        }
        else return patientLastLoginDate.compareTo(lastConsultationDate) < 0;
    }

    //Date -> LocalDate
    public LocalDate convertToLocalDate(Date dateToConvert) {
        return LocalDate.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }

    //LocalDate -> Date
    public Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}

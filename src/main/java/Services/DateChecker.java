package Services;

//Class for checking a 30 day timeframe between 2 dates
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DateChecker {

    //This method will be used to find out if a patient did not
    //have a consultation the past 30 days
    public boolean compareDates(LocalDate newDate, LocalDate oldDate){
        //some sql query to get newDate,oldDate
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
}

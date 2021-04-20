package team4.Sacchon.representation;

import antlr.Utils;
import lombok.Data;
import lombok.NoArgsConstructor;
import team4.Sacchon.model.Measurement;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class MeasurementRepresentation {

    private int id;
    private String date;
    private String time;
    private int glucoseLevel;
    private int carbIntake;
    private int patientId;
    private String uri;

    public MeasurementRepresentation(Measurement measurement) {
        if (measurement != null) {
            date = new SimpleDateFormat("dd/MM/yyyy").format(measurement.getDate());
            time = new SimpleDateFormat("HH:mm:ss").format(measurement.getDate());
            glucoseLevel = measurement.getGlucoseLevel();
            carbIntake = measurement.getCarbIntake();
            if (measurement.getPatient() != null)
                patientId = measurement.getPatient().getId();
            uri = "http://localhost:9000/v1/measurement/" + measurement.getId();
        }
    }

    public Measurement createMeasurement() throws Exception{
        Measurement measurement = new Measurement();
        measurement.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date + " " + time + ":00"));
        measurement.setGlucoseLevel(glucoseLevel);
        measurement.setCarbIntake(carbIntake);
        return measurement;
    }
}

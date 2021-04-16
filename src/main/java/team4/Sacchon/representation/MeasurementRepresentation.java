package team4.Sacchon.representation;

import lombok.Data;
import lombok.NoArgsConstructor;
import team4.Sacchon.model.Measurement;

import java.util.Date;

@Data
@NoArgsConstructor
public class MeasurementRepresentation {

    private int id;
    private Date date;
    private int glucoseLevel;
    private int carbIntake;
    private int patientId;
    private String uri;

    public MeasurementRepresentation(Measurement measurement) {
        if (measurement != null) {
            date = measurement.getDate();
            glucoseLevel = measurement.getGlucoseLevel();
            carbIntake = measurement.getCarbIntake();
            if (measurement.getPatient() != null)
                patientId = measurement.getPatient().getId();
            uri = "http://localhost:9000/v1/measurement/" + measurement.getId();
        }
    }

    public Measurement createMeasurement() {
        Measurement measurement = new Measurement();
        measurement.setDate(date);
        measurement.setGlucoseLevel(glucoseLevel);
        measurement.setCarbIntake(carbIntake);
        return measurement;
    }
}

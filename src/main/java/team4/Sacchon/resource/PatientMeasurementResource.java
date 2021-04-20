package team4.Sacchon.resource;

import org.restlet.resource.Delete;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Credentials;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.CredentialsRepository;
import team4.Sacchon.repository.MeasurementRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.MeasurementRepresentation;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PatientMeasurementResource extends ServerResource {

    private String patientUsername;
    private int measurementId;

    @Override
    protected void doInit() {
        patientUsername = getAttribute("username");
        measurementId = Integer.parseInt(getAttribute("id"));
    }

    @Delete("json")
    public ApiResult<Object> deleteMeasurement() {
        EntityManager em = JpaUtil.getEntityManager();
        MeasurementRepository measurementRepository = new MeasurementRepository(em);
        Measurement measurement = measurementRepository.getById(measurementId);
        if (measurement == null)
            return new ApiResult<>(null, 400, "Measurement does not exist.");

        if (measurement.getPatient().getUsername().equals(patientUsername)) {
            if (measurementRepository.delete(measurementId)) {
                return new ApiResult<>(null, 200, "Measurement deleted");
            } else {
                return new ApiResult<>(null, 400, "Measurement could not be deleted");
            }
        } else {
            return new ApiResult<>(null, 400, "Measurement does not belong to the patient.");
        }
    }

    @Put("json")
    public ApiResult<MeasurementRepresentation> updateMeasurement(MeasurementRepresentation measurementRepresentation) {
        Date date;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(measurementRepresentation.getDate() + " " + measurementRepresentation.getTime() + ":00");
        } catch (ParseException e) {
            return new ApiResult<>(null, 400, "Invalid date given.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        MeasurementRepository measurementRepository = new MeasurementRepository(em);
        Measurement measurement = measurementRepository.read(measurementId);
        measurement.setDate(date);
        measurement.setCarbIntake(measurementRepresentation.getCarbIntake());
        measurement.setGlucoseLevel(measurementRepresentation.getGlucoseLevel());
        measurementRepository.update(measurement);
        return new ApiResult<>(measurementRepresentation, 200, "Update successfull");
    }
}

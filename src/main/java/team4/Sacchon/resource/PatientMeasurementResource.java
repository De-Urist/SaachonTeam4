package team4.Sacchon.resource;

import org.restlet.resource.Delete;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.repository.MeasurementRepository;
import team4.Sacchon.representation.MeasurementRepresentation;
import team4.Sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static team4.Sacchon.resource.ResourceUtils.checkMeasurementInformation;

public class PatientMeasurementResource extends ServerResource {

    private int patientId;
    private int measurementId;

    @Override
    protected void doInit() {
        patientId = Integer.parseInt(getAttribute("id"));
        measurementId = Integer.parseInt(getAttribute("measurement_id"));
    }

    @Delete("json")
    public ApiResult<Object> deleteMeasurement() {
        ApiResult<Object> privileges = checkPatientPrivileges();
        if (privileges != null)
            return privileges;

        EntityManager em = JpaUtil.getEntityManager();
        MeasurementRepository measurementRepository = new MeasurementRepository(em);
        Measurement measurement = measurementRepository.getById(measurementId);
        if (measurement == null)
            return new ApiResult<>(null, 400, "Measurement does not exist.");

        if (measurement.getPatient().getId() == patientId) {
            if (measurementRepository.delete(measurementId)) {
                return new ApiResult<>(null, 200, "Measurement deleted");
            } else {
                return new ApiResult<>(null, 400, "Measurement could not be deleted");
            }
        } else {
            return new ApiResult<>(null, 400, "Measurement does not belong to this patient.");
        }
    }

    @Put("json")
    public ApiResult<Object> updateMeasurement(MeasurementRepresentation measurementRepresentation) {
        ApiResult<Object> privileges = checkPatientPrivileges();
        if (privileges != null)
            return privileges;

        ApiResult<Object> checkedMeasurement = checkMeasurementInformation(measurementRepresentation);
        if (checkedMeasurement != null) {
            return checkedMeasurement;
        }

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
        return new ApiResult<>(measurementRepresentation, 200, "Update successful");
    }

    private ApiResult<Object> checkPatientPrivileges(){
        try {
            ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        }catch (AuthorizationException e) {
            return new ApiResult<>(null, 403, "Forbidden");
        }
        return null;
    }
}

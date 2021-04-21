package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.MeasurementRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.MeasurementRepresentation;
import team4.Sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static team4.Sacchon.resource.ResourceUtils.checkMeasurementInformation;

public class PatientMeasurementListResource extends ServerResource {

    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Post("json")
    public ApiResult<Object> addMeasurement(MeasurementRepresentation measurementRepresentation) {
        ApiResult<Object> privileges = checkPatientPrivileges();
        if (privileges != null)
            return privileges;

        ApiResult<Object> checkedMeasurement = checkMeasurementInformation(measurementRepresentation);
        if (checkedMeasurement != null) {
            return checkedMeasurement;
        }

        Measurement measurement;
        try {
            measurement = measurementRepresentation.createMeasurement();
        } catch (Exception e) {
            return new ApiResult<>(null, 400, "Could not create measurement");
        }

        EntityManager em = JpaUtil.getEntityManager();
        Patient patient = new PatientRepository(em).read(id);
        measurement.setPatient(patient);
        new MeasurementRepository(em).save(measurement);
        em.close();
        return new ApiResult<>(measurementRepresentation, 200, "Measurement has been added");
    }

    @Get("json")
    public ApiResult<Object> getPatientMeasurements() {
        Date fromDate = null;
        Date toDate = null;

        if (getQueryValue("fromDate") != null || getQueryValue("toDate") != null) {
            try {
                fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(getQueryValue("fromDate"));
                toDate = new SimpleDateFormat("dd/MM/yyyy").parse(getQueryValue("toDate"));
            } catch (Exception e) {
                return new ApiResult<>(null, 400, "Both dates should be present with format: dd/MM/yyyy");
            }
        }

        return getPatientMeasurements(fromDate, toDate);
    }

    private ApiResult<Object> getPatientMeasurements(Date fromDate, Date toDate) {
        EntityManager em = JpaUtil.getEntityManager();
        MeasurementRepository measurementRepository = new MeasurementRepository(em);
        List<Measurement> measurements;
        if (fromDate == null || toDate == null) {
            measurements = measurementRepository.getMeasurementsOf(id);
        } else {
            measurements = measurementRepository.getMeasurementsOfIdBetween(id, fromDate, toDate);
        }

        List<MeasurementRepresentation> measurementRepresentationList = new ArrayList<>();
        for (Measurement m : measurements) {
            measurementRepresentationList.add(new MeasurementRepresentation(m));
        }
        em.close();
        return new ApiResult<>(measurementRepresentationList, 200, "Past patient measurements");
    }

    private ApiResult<Object> checkPatientPrivileges() {
        try {
            ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        } catch (AuthorizationException e) {
            return new ApiResult<>(null, 403, "Forbidden");
        }
        return null;
    }
}

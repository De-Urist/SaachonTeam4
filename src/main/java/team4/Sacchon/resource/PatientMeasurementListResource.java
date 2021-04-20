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

    /**
     * Returns all past patient measurements if no query parameters have been passed.
     * Otherwise it uses the fromDate, toDate and type parameters to call the corresponding queries.
     */
    @Get("json")
    public ApiResult<Object> getPatientMeasurements() {
        ApiResult<Object> privileges = checkPatientPrivileges();
        if (privileges != null)
            return privileges;

        Date fromDate = null;
        Date toDate = null;
        String type = null;

        try {
            fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(getQueryValue("fromDate"));
            toDate = new SimpleDateFormat("dd/MM/yyyy").parse(getQueryValue("toDate"));
            type = getQueryValue("type");

            if (type != null && (!"carb".equals(type) && !"glucose".equals(type)))
                return new ApiResult<>(null, 400, "No such data type is logged.");
        } catch (Exception e) {
        }

        EntityManager em = JpaUtil.getEntityManager();
        Patient patient = new PatientRepository(em).read(id);
        MeasurementRepository measurementRepository = new MeasurementRepository(em);

        //if not all date info is present return all past measurements
        if (fromDate == null || toDate == null) {
            return getAllPatientMeasurements(measurementRepository, patient);
        }
        //else return the average glucose or carb for that period
        else {
            Double average = 0.0;
            if (type.equals("glucose")) {
                average = measurementRepository.getAverageGlucoseOfMeasurements(patient.getId(), fromDate, toDate);
                return new ApiResult<>(average, 200, "Average glucose levels of patient.");
            } else {
                average = measurementRepository.getAverageCarbOfMeasurements(patient.getId(), fromDate, toDate);
                return new ApiResult<>(average, 200, "Average carb intake levels of patient.");
            }
        }
    }

    private ApiResult<Object> getAllPatientMeasurements(MeasurementRepository measurementRepository, Patient patient) {
        List<Measurement> measurements = measurementRepository.getMeasurementsOf(patient.getId());
        List<MeasurementRepresentation> measurementRepresentationList = new ArrayList<>();
        for (Measurement m : measurements) {
            measurementRepresentationList.add(new MeasurementRepresentation(m));
        }
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

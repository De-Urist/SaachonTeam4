package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.MeasurementRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.MeasurementRepresentation;
import team4.Sacchon.representation.PatientRepresentation;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientMeasurementResource extends ServerResource {

    private String username;

    @Override
    protected void doInit() {
        username = getAttribute("username");
    }

    @Post("json")
    public ApiResult<MeasurementRepresentation> addMeasurement(MeasurementRepresentation measurementRepresentation){
        if (measurementRepresentation == null)
            return new ApiResult<>(null, 400, "No input data to create the measurement");
        if (measurementRepresentation.getDate() == null)
            return new ApiResult<>(null, 400, "No date was given to create the measurement");
        if (measurementRepresentation.getGlucoseLevel() == 0)
            return new ApiResult<>(null, 400, "No glucose data was given to create the measurement");
        if (measurementRepresentation.getCarbIntake() == 0)
            return new ApiResult<>(null, 400, "No carb data was given to create the measurement");

        Measurement measurement;

        try{
             measurement = measurementRepresentation.createMeasurement();

            }catch (Exception e) {
            return new ApiResult<>(null, 400, "Could not create measurement");
        }
        EntityManager em = JpaUtil.getEntityManager();
        //get the current patient via username
        Patient patient = new PatientRepository(em).getByUsername(username);
        //set the patient
        measurement.setPatient(patient);
        //save the measurement
        new MeasurementRepository(em).save(measurement);
        em.close();
        return new ApiResult<>(measurementRepresentation, 200, "Measurement has been added");
    }

    @Get("json")
    public ApiResult<Object> getPatientMeasurements(){
        Date fromDate = null;
        Date toDate = null;
        String type = null;

        try{
            fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(getQueryValue("fromDate"));
            toDate = new SimpleDateFormat("dd/MM/yyyy").parse(getQueryValue("toDate"));
            type = getQueryValue("type");
        } catch (Exception e){

        }

        if(!"carb".equals(type) && !"glucose".equals(type))
            return new ApiResult<>(null, 400, "No such data type is logged.");

        EntityManager em = JpaUtil.getEntityManager();
        Patient patient = new PatientRepository(em).getByUsername(username);
        MeasurementRepository measurementRepository = new MeasurementRepository(em);

        if (fromDate == null || toDate == null) {
            List<Measurement> measurements = measurementRepository.getMeasurementsOf(patient.getId());
            em.close();
            List<MeasurementRepresentation> measurementRepresentationList = new ArrayList<>();
            for(Measurement m : measurements){
                measurementRepresentationList.add(new MeasurementRepresentation(m));
            }
            return new ApiResult<>(measurementRepresentationList, 200, "All patient measurements");
        } else {
            Double average =0.0;
            if (type.equals("carb")) {
                average = measurementRepository.getAverageGlucoseOfMeasurements(patient.getId(), fromDate, toDate);
                return new ApiResult<>(average, 200, "Average glucose levels of patient.");
            }
            else {
                average = measurementRepository.getAverageCarbOfMeasurements(patient.getId(), fromDate, toDate);
                return new ApiResult<>(average, 200, "Average carb intake levels of patient.");
            }

        }
    }
}

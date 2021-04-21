package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.repository.MeasurementRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PatientMeasurementTypeResource extends ServerResource {

    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Get("json")
    public ApiResult<Object> getPatientAverageMeasurements() {
        Date fromDate;
        Date toDate;
        String type;

        try {
            fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(getQueryValue("fromDate"));
            toDate = new SimpleDateFormat("dd/MM/yyyy").parse(getQueryValue("toDate"));
        } catch (Exception e) {
            return new ApiResult<>(null, 400, "Invalid date");
        }

        try {
            type = getQueryValue("type");
            if (type != null && (!"carb".equals(type) && !"glucose".equals(type)))
                return new ApiResult<>(null, 400, "No such data type is logged.");
        } catch (Exception e) {
            return new ApiResult<>(null, 400, "Invalid data type.");
        }

        Double average = 0.0;
        MeasurementRepository measurementRepository = new MeasurementRepository(JpaUtil.getEntityManager());
        if (type.equals("glucose")) {
            average = measurementRepository.getAverageGlucoseOfMeasurements(id, fromDate, toDate);
            return new ApiResult<>(average, 200, "Average glucose levels of patient.");
        } else {
            average = measurementRepository.getAverageCarbOfMeasurements(id, fromDate, toDate);
            return new ApiResult<>(average, 200, "Average carb intake levels of patient.");
        }
    }
}

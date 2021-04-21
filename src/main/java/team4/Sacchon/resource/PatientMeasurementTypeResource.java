//package team4.Sacchon.resource;
//
//import org.restlet.resource.ServerResource;
//
//public class PatientMeasurementTypeResource extends ServerResource {
//
//    public void hello() {
//        String type = null;
//
//        type = getQueryValue("type");
//
//        if (type != null && (!"carb".equals(type) && !"glucose".equals(type)))
//            return new ApiResult<>(null, 400, "No such data type is logged.");
//
//        else {
//            Double average = 0.0;
//            if (type.equals("glucose")) {
//                average = measurementRepository.getAverageGlucoseOfMeasurements(patient.getId(), fromDate, toDate);
//                return new ApiResult<>(average, 200, "Average glucose levels of patient.");
//            } else {
//                average = measurementRepository.getAverageCarbOfMeasurements(patient.getId(), fromDate, toDate);
//                return new ApiResult<>(average, 200, "Average carb intake levels of patient.");
//            }
//        }
//    }
//}

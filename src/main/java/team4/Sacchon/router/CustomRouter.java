package team4.Sacchon.router;

import org.restlet.Application;
import org.restlet.routing.Router;
import team4.Sacchon.resource.*;

public class CustomRouter {
    private Application application;

    public CustomRouter(Application application) {
        this.application = application;
    }

    public Router publicResources() {
        Router router = new Router();
        router.attach("/ping", PingServerResource.class);
        router.attach("/register_patient", RegisterPatientResource.class);
        router.attach("/register_doctor", RegisterDoctorResource.class);
        router.attach("/register_chief", RegisterChiefDoctorResource.class);
        return router;
    }

    public Router protectedResources() {
        Router router = new Router();
        router.attach("/validate/{username}", ValidateResource.class);
        router.attach("/patient/{id}", PatientResource.class);
        router.attach("/patient/{id}/measurement", PatientMeasurementListResource.class);
        router.attach("/patient/{id}/measurement_type", PatientMeasurementTypeResource.class);
        router.attach("/patient/{id}/measurement/{measurement_id}", PatientMeasurementResource.class);
        router.attach("/patient", PatientListResource.class);
        router.attach("/doctor", DoctorListResource.class);

        router.attach("/doctor/{id}", DoctorResource.class);
//        router.attach("/chief", ChiefDoctorListResource.class);
        router.attach("/chief/{id}", ChiefDoctorResource.class);
//        router.attach("/consultation", ConsultationListResource.class);
        router.attach("/consultation/{id}", ConsultationResource.class);
//        router.attach("/measurement", MeasurementListResource.class);
        router.attach("/measurement/{id}", MeasurementResource.class);
        return router;
    }
}

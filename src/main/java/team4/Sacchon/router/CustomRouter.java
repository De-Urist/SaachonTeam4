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
        router.attach("/registerpatient", RegisterPatientResource.class);
        router.attach("/registerdoctor", RegisterDoctorResource.class);
        router.attach("/registerchief", RegisterChiefResource.class);

        return router;
    }

    public Router protectedResources() {
        Router router = new Router();
        router.attach("/patient", PatientListResource.class);
        router.attach("/patient/{username}", PatientResource.class);
        router.attach("/patient/{username}/measurement", PatientMeasurementListResource.class);
        router.attach("/patient/{username}/measurement/{id}", PatientMeasurementResource.class);
        router.attach("/validate", ValidateResource.class);
//        router.attach("/doctor", DoctorListResource.class);
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

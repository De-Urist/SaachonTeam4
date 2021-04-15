package team4.Sacchon.router;

import org.restlet.Application;
import org.restlet.routing.Router;
import team4.Sacchon.resource.*;

public class CustomRouter {
    private Application application;

    public CustomRouter(Application application){
        this.application = application;
    }
    public Router publicResources(){
        Router router = new Router();
        //Login for different users (Doctor,Patient,ChiefDoctor)
        //router.attach("/login", LoginResource.class)

        //Similarly for signup
        //router.attach("/signup", LoginResource.class)

        //Patients
        router.attach("/patient", PatientListResource.class);
        router.attach("/patient/{id}", PatientResource.class);

        //Doctors
        router.attach("/doctor", DoctorResource.class);
        router.attach("/doctor/{id}", DoctorResource.class);

        //ChiefDoctors
        router.attach("/chief", ChiefDoctorResource.class);
        router.attach("/chief/{id}", ChiefDoctorResource.class);

        //Consultations
        router.attach("/consultation", ConsultationResource.class);
        router.attach("/consultation/{id}", ConsultationResource.class);

        //Measurements
        router.attach("/measurement", MeasurementResource.class);
        router.attach("/measurement/{id}", MeasurementResource.class);

        //TEST
        router.attach("/ping", PingServerResource.class);
        return router;
    }
}

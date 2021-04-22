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
        router.attach("/doctor/{id}/consultation", DoctorConsultationListResource.class);
        router.attach("/waiting_patients", WaitingPatientsResource.class);
        router.attach("/patient/{id}/consultation_last", PatientLastConsultationResource.class);
        router.attach("/patient/{id}/consultation", PatientConsultationResource.class);
        router.attach("/patient/{id}/consultation_updates", PatientConsultationUpdatesResource.class);
        router.attach("/patients_with_no_consultation/{id}", PatientListWithNoConsResource.class);
        router.attach("/doctor/{id}", DoctorResource.class);
        router.attach("/doctor/{id}/patient", DoctorPatientListResource.class);

        router.attach("/consultation/{id}", ConsultationResource.class);
        return router;
    }
}

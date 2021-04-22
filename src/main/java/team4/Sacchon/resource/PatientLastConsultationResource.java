package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.repository.MeasurementRepository;
import team4.Sacchon.representation.ConsultationRepresentation;
import team4.Sacchon.representation.MeasurementRepresentation;
import team4.Sacchon.security.Shield;

import javax.persistence.EntityManager;

public class PatientLastConsultationResource extends ServerResource {

    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Get("json")
    public ApiResult<Object> getLastPatientConsultation() {
        ApiResult<Object> privileges = checkChiefDoctorPrivileges();
        if (privileges != null)
            return privileges;

        EntityManager em = JpaUtil.getEntityManager();
        Consultation consultation = new ChiefDoctorRepository(em).getPatientLastConsultation(id);
        if (consultation == null) {
            Measurement measurement = new MeasurementRepository(em).getPatientFirstMeasurement(id);
            if (measurement == null)
                return new ApiResult<>(null, 400, "This patient has not had any measurements or consultations");
            return new ApiResult<>(new MeasurementRepresentation(measurement), 201, "Patient without consultations.");
        }
        return new ApiResult<>(new ConsultationRepresentation(consultation), 200, "Last patient consultation.");
    }

    private ApiResult<Object> checkChiefDoctorPrivileges(){
        try {
            ResourceUtils.checkRole(this, Shield.ROLE_CHIEF);
        }catch (AuthorizationException e) {
            return new ApiResult<>(null, 403, "Forbidden");
        }
        return null;
    }
}

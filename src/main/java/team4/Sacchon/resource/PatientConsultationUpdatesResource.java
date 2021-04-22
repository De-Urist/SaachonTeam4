package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.repository.MeasurementRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.ConsultationRepresentation;
import team4.Sacchon.representation.MeasurementRepresentation;
import team4.Sacchon.security.Shield;

import javax.persistence.EntityManager;

public class PatientConsultationUpdatesResource extends ServerResource {

    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Get("json")
    public ApiResult<Object> checkForPatientConsultationUpdates() {
        ApiResult<Object> privileges = checkPatientPrivileges();
        if (privileges != null)
            return privileges;

        EntityManager em = JpaUtil.getEntityManager();
        Consultation consultation = new PatientRepository(em).getLastModifiedConsultationForPatient(id);
        Patient patient = new PatientRepository(em).read(id);
        if (consultation.getLastModified() != null && consultation.getLastModified().after(patient.getLastLogin())) {
            return new ApiResult<>(new ConsultationRepresentation(consultation), 200, "There was an updated consultation.");
        }
        return new ApiResult<>(null, 400, "No updated consultations found.");
    }

    private ApiResult<Object> checkPatientPrivileges(){
        try {
            ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        }catch (AuthorizationException e) {
            return new ApiResult<>(null, 403, "Forbidden");
        }
        return null;
    }
}

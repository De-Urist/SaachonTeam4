package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.repository.ConsultationRepository;
import team4.Sacchon.repository.MeasurementRepository;
import team4.Sacchon.representation.ConsultationRepresentation;
import team4.Sacchon.representation.MeasurementRepresentation;
import team4.Sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class PatientConsultationResource extends ServerResource {

    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Get("json")
    public ApiResult<Object> getPatientConsultations() {
        ApiResult<Object> privileges = checkPatientPrivileges();
        if (privileges != null)
            return privileges;

        EntityManager em = JpaUtil.getEntityManager();
        List<Consultation> consultationList = new ConsultationRepository(em).getByPatientId(id);
        if (consultationList.size() == 0) {
            return new ApiResult<>(null, 400, "Patient without consultations.");
        }
        List<ConsultationRepresentation> consultationRepresentationList = new ArrayList<>();
        for (Consultation c : consultationList){
            consultationRepresentationList.add(new ConsultationRepresentation(c));
        }
        return new ApiResult<>(consultationRepresentationList, 200, "All patient consultations.");
    }

    private ApiResult<Object> checkPatientPrivileges(){
        try {
            ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        }catch (AuthorizationException patientException) {
            try {
                ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
            } catch (AuthorizationException doctorException) {
                return new ApiResult<>(null, 403, "Forbidden");
            }
        }
        return null;
    }
}

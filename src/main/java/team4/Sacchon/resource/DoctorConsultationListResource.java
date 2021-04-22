package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.*;
import team4.Sacchon.representation.ConsultationRepresentation;
import team4.Sacchon.representation.MeasurementRepresentation;
import team4.Sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static team4.Sacchon.resource.ResourceUtils.checkConsultationInformation;

public class DoctorConsultationListResource extends ServerResource {

    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Get("json")
    public ApiResult<Object> getDoctorConsultations() {
        Date fromDate = null;
        Date toDate = null;

        if (getQueryValue("fromDate") != null || getQueryValue("toDate") != null) {
            try {
                fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(getQueryValue("fromDate"));
                toDate = new SimpleDateFormat("dd/MM/yyyy").parse(getQueryValue("toDate"));
            } catch (Exception e) {
                return new ApiResult<>(null, 400, "Both dates should be present with format: dd/MM/yyyy");
            }
        }

        return getDoctorConsultationsBetween(fromDate, toDate);
    }

    private ApiResult<Object> getDoctorConsultationsBetween(Date fromDate, Date toDate) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Consultation> consultations;
        if (fromDate == null || toDate == null) {
            consultations = new ConsultationRepository(em).getByDoctorId(id);
        } else {
            consultations = new ChiefDoctorRepository(em).getConsultationsByDoctorIdAndDates(id, fromDate, toDate);
        }
        if (consultations.size() != 0) {
            List<ConsultationRepresentation> consultationRepresentationList = new ArrayList<>();
            for (Consultation c : consultations) {
                consultationRepresentationList.add(new ConsultationRepresentation(c));
            }
            em.close();
            return new ApiResult<>(consultationRepresentationList, 200, "Past doctor consultations");
        }else {
            em.close();
            return new ApiResult<>(null, 400, "The doctor has no consultations.");
        }
    }

    @Post("json")
    public ApiResult<Object> add(ConsultationRepresentation consultationRepresentation){
        ApiResult<Object> privileges = checkDoctorPrivileges();
        if (privileges != null)
            return privileges;

        ApiResult<Object> checkedMeasurement = checkConsultationInformation(consultationRepresentation);
        if (checkedMeasurement != null) {
            return checkedMeasurement;
        }

        Consultation c ;
        try {
            c = consultationRepresentation.createConsultation();
        }
        catch (Exception e){
            return new ApiResult<>(null, 400, "Could not create consultation");
        }
        EntityManager em = JpaUtil.getEntityManager();
        Doctor doctor = new DoctorRepository(em).read(id);
        c.setPatient(new PatientRepository(em).read(consultationRepresentation.getPatientId()));
        c.setDoctor(doctor);
        new ConsultationRepository(em).save(c);
        Patient patient = c.getPatient();
        patient.setDoctor(doctor);
        new PatientRepository(em).save(patient);
        em.close();
        return new ApiResult<>(consultationRepresentation, 200, "Consultation has been added");
    }

    private ApiResult<Object> checkDoctorPrivileges() {
        try {
            ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        } catch (AuthorizationException e) {
            return new ApiResult<>(null, 403, "Forbidden");
        }
        return null;
    }
}

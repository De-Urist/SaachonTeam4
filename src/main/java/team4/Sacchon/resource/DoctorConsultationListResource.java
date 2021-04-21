package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Consultation;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.repository.ConsultationRepository;
import team4.Sacchon.repository.MeasurementRepository;
import team4.Sacchon.representation.ConsultationRepresentation;
import team4.Sacchon.representation.MeasurementRepresentation;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        List<ConsultationRepresentation> consultationRepresentationList = new ArrayList<>();
        for (Consultation c : consultations) {
            consultationRepresentationList.add(new ConsultationRepresentation(c));
        }
        em.close();
        return new ApiResult<>(consultationRepresentationList, 200, "Past doctor consultations");
    }
}

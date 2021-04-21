package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.representation.DoctorRepresentation;
import team4.Sacchon.representation.PatientRepresentation;
import team4.Sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorListResource extends ServerResource {

    @Get("json")
    public ApiResult<Object> getInactiveDoctors() {
        ApiResult<Object> privileges = checkChiefDoctorPrivileges();
        if (privileges != null)
            return privileges;

        Date fromDate;
        Date toDate;

        try {
            fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(getQueryValue("fromDate"));
            toDate = new SimpleDateFormat("dd/MM/yyyy").parse(getQueryValue("toDate"));
        } catch (Exception e) {
            return new ApiResult<>(null, 400, "Both dates should be present with format: dd/MM/yyyy");
        }

        EntityManager em = JpaUtil.getEntityManager();
        List<Doctor> doctors = new ChiefDoctorRepository(em).getInactiveDoctors(fromDate, toDate);
        em.close();
        List<DoctorRepresentation> doctorRepresentations = doctors.stream()
                .map(DoctorRepresentation::new)
                .collect(Collectors.toList());
        return new ApiResult<>(doctorRepresentations, 200, "All inactive doctors");
    }

    private ApiResult<Object> checkChiefDoctorPrivileges() {
        try {
            ResourceUtils.checkRole(this, Shield.ROLE_CHIEF);
        } catch (AuthorizationException e) {
            return new ApiResult<>(null, 403, "Forbidden");
        }
        return null;
    }
}

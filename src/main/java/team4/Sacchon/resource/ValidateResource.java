package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.ChiefDoctor;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.repository.DoctorRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.ChiefDoctorRepresentation;
import team4.Sacchon.representation.DoctorRepresentation;
import team4.Sacchon.representation.PatientRepresentation;
import team4.Sacchon.security.Shield;

public class ValidateResource extends ServerResource {
    private String username;

    @Override
    protected void doInit() {
        username = getAttribute("username");
    }

    @Get("json")
    public ApiResult<Object> validateUser() {
        try {
            ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
            return new ApiResult<>(getPatientRepresentation(), 200, "Patient found");
        } catch (AuthorizationException patientException) {
            try {
                ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
                return new ApiResult<>(getDoctorRepresentation(), 200, "Doctor found");
            } catch (AuthorizationException doctorException) {
                try {
                    ResourceUtils.checkRole(this, Shield.ROLE_CHIEF);
                    return new ApiResult<>(getChiefDoctorRepresentation(), 200, "Doctor found");
                } catch (AuthorizationException chiefDoctorException) {
                    return new ApiResult<>(null, 500, "No account was found");
                }
            }
        }
    }

    private PatientRepresentation getPatientRepresentation() {
        PatientRepository patientRepository = new PatientRepository(JpaUtil.getEntityManager());
        Patient patient = patientRepository.getByUsername(username);
        return new PatientRepresentation(patient);
    }

    private DoctorRepresentation getDoctorRepresentation() {
        DoctorRepository doctorRepository = new DoctorRepository(JpaUtil.getEntityManager());
        Doctor doctor = doctorRepository.getByUsername(username);
        return new DoctorRepresentation(doctor);
    }

    private ChiefDoctorRepresentation getChiefDoctorRepresentation() {
        ChiefDoctorRepository chiefDoctorRepository = new ChiefDoctorRepository(JpaUtil.getEntityManager());
        ChiefDoctor chiefDoctor = chiefDoctorRepository.getByUsername(username);
        return new ChiefDoctorRepresentation(chiefDoctor);
    }
}

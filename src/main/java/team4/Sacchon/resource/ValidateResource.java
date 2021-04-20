package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.PatientRepresentation;
import team4.Sacchon.security.Shield;

import javax.persistence.EntityManager;

public class ValidateResource extends ServerResource {

    @Get("json")
    public ApiResult<Object> validateUser(){

        try {
            ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
            return new ApiResult<>(null, 200, "patient");
        } catch (AuthorizationException e) {
            try {
                ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
                return new ApiResult<>(null, 200, "doctor");
            } catch (AuthorizationException authorizationException) {
                try {
                    ResourceUtils.checkRole(this, Shield.ROLE_CHIEF);
                    return new ApiResult<>(null, 200, "chief");
                } catch (AuthorizationException exception) {
                    return new ApiResult<>(null, 500, "not found");
                }
            }
        }
    }
}

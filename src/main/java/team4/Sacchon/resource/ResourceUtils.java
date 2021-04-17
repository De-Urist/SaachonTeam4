package team4.Sacchon.resource;

import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;

public class ResourceUtils {

    public static void checkRole(ServerResource serverResource, String role) throws AuthorizationException {
        if (!serverResource.isInRole(role)) {
            throw new AuthorizationException("You're not authorized to send this call.");
        }
    }
}
package team4.Sacchon.resource;

import org.restlet.resource.ServerResource;
import team4.Sacchon.exception.AuthorizationException;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Credentials;
import team4.Sacchon.repository.CredentialsRepository;

import javax.persistence.EntityManager;

public class ResourceUtils {

    public static void checkRole(ServerResource serverResource, String role) throws AuthorizationException {
        if (!serverResource.isInRole(role)) {
            throw new AuthorizationException("You're not authorized to send this call.");
        }
    }

    protected static boolean usernameExistsInCredentials(String candidateUsername) {
        EntityManager em = JpaUtil.getEntityManager();
        CredentialsRepository credentialsRepository = new CredentialsRepository(em);
        Credentials credentials = credentialsRepository.getByUsername(candidateUsername);
        em.close();
        return credentials != null;
    }
}
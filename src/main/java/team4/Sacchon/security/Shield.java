package team4.Sacchon.security;

import org.restlet.Application;
import org.restlet.data.ChallengeScheme;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Verifier;

public class Shield {

    public static final String ROLE_ADMIN = "chief";
    public static final String ROLE_OWNER = "owner";
    public static final String ROLE_USER = "user";

    private Application app;

    public Shield(Application app){
        this.app = app;
    }

    public ChallengeAuthenticator createApiGuard(){
        ChallengeAuthenticator apiGuard = new ChallengeAuthenticator(
                app.getContext(), ChallengeScheme.HTTP_BASIC, "real");
        Verifier verifier = new CustomVerifier();
        apiGuard.setVerifier(verifier);

        return apiGuard;
    }
}

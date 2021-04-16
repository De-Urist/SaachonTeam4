package team4.Sacchon.security;

import org.restlet.Application;
import org.restlet.data.ChallengeScheme;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Verifier;

public class Shield {

    public static final String ROLE_CHIEF = "chief";
    public static final String ROLE_DOCTOR = "doctor";
    public static final String ROLE_PATIENT = "patient";

    private Application app;

    public Shield(Application app){
        this.app = app;
    }

    public ChallengeAuthenticator createApiGuard(){
        ChallengeAuthenticator apiGuard = new ChallengeAuthenticator(
                app.getContext(), ChallengeScheme.HTTP_BASIC, "realm");
        Verifier verifier = new CustomVerifier();
        apiGuard.setVerifier(verifier);

        return apiGuard;
    }
}

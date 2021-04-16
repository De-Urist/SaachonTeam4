package team4.Sacchon;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Role;
import team4.Sacchon.router.CustomRouter;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.security.CorsFilter;
import team4.Sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.logging.Logger;

public class Main extends Application {

    public static final Logger LOGGER = Engine.getLogger(Main.class);

    public Main(){
        setName("SacchonApplication");

        getRoles().add(new Role(this, Shield.ROLE_CHIEF));
        getRoles().add(new Role(this, Shield.ROLE_DOCTOR));
        getRoles().add(new Role(this, Shield.ROLE_PATIENT));
    }

    public static void main(String[] args) throws Exception {
        EntityManager em = JpaUtil.getEntityManager();
        System.out.println("Connection established");
        //Entity Manager commands for testing
        em.close();

        Component c = new Component();
        c.getServers().add(Protocol.HTTP, 9000);
        c.getDefaultHost().attach("/v1", new Main());
        c.start();

        LOGGER.info("Sample Web API started");
        //LOGGER.info("URL: http://localhost:9000/v1/doctor");
        LOGGER.info("URL: http://localhost:9000/v1/patient");
    }

    @Override
    public Restlet createInboundRoot() {
        CustomRouter customRouter = new CustomRouter(this);
        Shield shield = new Shield(this);
        Router publicRouter = customRouter.publicResources();
        ChallengeAuthenticator apiGuard = shield.createApiGuard();
        // Create the api router, protected by a guard
        Router protectedRouter = customRouter.protectedResources();
        apiGuard.setNext(protectedRouter);
        publicRouter.attachDefault(apiGuard);
        // return publicRouter;
        CorsFilter corsFilter = new CorsFilter(this);
        return corsFilter.createCorsFilter(publicRouter);
    }
}

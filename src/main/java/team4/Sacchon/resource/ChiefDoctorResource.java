package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.ChiefDoctor;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.representation.ChiefDoctorRepresentation;


import javax.persistence.EntityManager;

public class ChiefDoctorResource extends ServerResource {
    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Get("json")
    public ChiefDoctorRepresentation getChiefDoctor() {
        EntityManager em = JpaUtil.getEntityManager();
        ChiefDoctorRepository chiefDoctorRepository = new  ChiefDoctorRepository(em);
        ChiefDoctor chief = chiefDoctorRepository.read(id);

        ChiefDoctorRepresentation chiefDoctorRepresentation = new  ChiefDoctorRepresentation(chief);
        em.close();
        return  chiefDoctorRepresentation;
    }
}

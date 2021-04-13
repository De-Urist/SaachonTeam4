package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.repository.DoctorRepository;
import team4.Sacchon.representation.DoctorRepresentation;

import javax.persistence.EntityManager;

public class DoctorResource extends ServerResource {
    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Get("json")
    public DoctorRepresentation getDoctor() {
        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        Doctor doctor = doctorRepository.read(id);

        DoctorRepresentation doctorRepresentation = new DoctorRepresentation(doctor);
        em.close();
        return doctorRepresentation;
    }
}

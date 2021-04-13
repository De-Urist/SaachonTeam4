package team4.Sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Measurement;
import team4.Sacchon.repository.MeasurementRepository;
import team4.Sacchon.representation.MeasurementRepresentation;

import javax.persistence.EntityManager;

public class MeasurementResource extends ServerResource {
    private int id;

    @Override
    protected void doInit() {
        id = Integer.parseInt(getAttribute("id"));
    }

    @Get("json")
    public MeasurementRepresentation getMeasurement() {
        EntityManager em = JpaUtil.getEntityManager();
        MeasurementRepository measurementRepository = new MeasurementRepository(em);
        Measurement measurement = measurementRepository.read(id);

        MeasurementRepresentation measurementRepresentation = new MeasurementRepresentation(measurement);
        em.close();
        return measurementRepresentation;
    }
}

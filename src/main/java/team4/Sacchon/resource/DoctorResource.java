package team4.Sacchon.resource;

import javassist.NotFoundException;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.repository.DoctorRepository;
import team4.Sacchon.repository.PatientRepository;
import team4.Sacchon.representation.DoctorRepresentation;
import team4.Sacchon.security.CustomRole;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<Doctor> doctor = doctorRepository.read(id);

        DoctorRepresentation doctorRepresentation = new DoctorRepresentation(doctor);
        em.close();
        return doctorRepresentation;
    }

    @Override
    public List<DoctorRepresentation> getDoctors(){
        ResourceUtils.checkRole(this, CustomRole.ROLE_CHIEFDOCTOR.getRoleName());
        List<Optional<Doctor>> doctors= doctorRepository.findAll();

        List<DoctorRepresentation> doctorRepresentationList = new ArrayList<>();

        doctors.forEach(doctor -> doctorRepresentationList.add(DoctorRepresentation.getDoctorRepresentation(doctor)));

        return doctorRepresentationList;
    }

    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private EntityManager em;
    private long id;

    @Override
    protected void doInit() throws ResourceException {
        try {
            em = SacchonJpa.getEntityManager();
            doctorRepository = new DoctorRepository(em);
            patientRepository = new PatientRepository(em);
            id = (int) Long.parseLong(getAttribute("id"));
        }
        catch(Exception ex){
            throw new ResourceException(ex);
        }
    }

    @Override
    protected void doRelease() throws ResourceException {
        em.close();
    }

    @Override
    public DoctorRepresentation getDoctor() throws NotFoundException {
        ResourceUtils.checkRole(this, CustomRole.ROLE_DOCTOR.getRoleName());
        Optional<Doctor> doctor = doctorRepository.findById(id);
        setExisting(doctor.isPresent());
        if (!doctor.isPresent())  throw new NotFoundException("Doctor is not found");
        DoctorRepresentation doctorRepresentation = DoctorRepresentation.getDoctorRepresentation(doctor.get());
        return doctorRepresentation;
    }

    @Override
    public DoctorRepresentation update(DoctorRepresentation doctorReprIn) throws NotFoundException, BadEntityException {
        ResourceUtils.checkRole(this, CustomRole.ROLE_DOCTOR.getRoleName());
        Optional<Doctor> doctorOpt = doctorRepository.findById(id);
        if (!doctorOpt.isPresent()) throw new NotFoundException("The given doctor id is not existing");
        Doctor doctor = doctorOpt.get();

        doctor.setFirstName(doctorReprIn.getFirstName());
        doctor.setLastName(doctorReprIn.getLastName());
        doctor.setPassword(doctorReprIn.getPassword());

        doctorRepository.save(doctor);
        return DoctorRepresentation.getDoctorRepresentation(doctor);
    }
    @Override
    public void remove() throws ResourceException, NotFoundException {
        ResourceUtils.checkRole(this, CustomRole.ROLE_DOCTOR.getRoleName());
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (!doctor.isPresent()) throw new NotFoundException("Non existing doctor");
        doctor.get().setActive(false);
        doctorRepository.save(doctor.get());

        TypedQuery<Patient> query= em.createQuery("FROM Patient P WHERE doctor_id=:param", Patient.class);
        query.setParameter("param",this.id);
        List<Patient> DoctorsPatients=query.getResultList();

        for (Patient patient: DoctorsPatients){
            patient.setDoctor(null);
            patientRepository.save(patient);
        }
    }

}











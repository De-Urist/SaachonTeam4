package team4.Sacchon.repository;

import team4.Sacchon.model.Doctor;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


public class DoctorRepository extends Repository<Optional<Doctor>, Integer> {

    private EntityManager entityManager;

    public DoctorRepository(EntityManager em) {
        this.entityManager = entityManager;
        super(em);
    }

    @Override
    public Class<Optional<Doctor>> getEntityClass() {
        return Doctor.class;
    }

    @Override
    public String getClassName() {
        return Doctor.class.getName();
    }
}


    public Optional<Doctor> findById(Long id) {
        Doctor doctor = entityManager.find(Doctor.class, id);
        return doctor != null ? Optional.of(doctor) : Optional.empty();
    }

    public List<Optional<Doctor>> findAll() {
        return entityManager.createQuery("from Doctor").getResultList();
    }

    public Optional<Doctor> findByName(String user_username) {
        Doctor doctor = entityManager.find(Doctor.class, user_username);
        return doctor != null ? Optional.of(doctor) : Optional.empty();
    }

    public Doctor findByUsername(String user_username) {
        Doctor doctor = entityManager.find(Doctor.class, user_username);
        return doctor;
    }

    public Optional<Doctor> findByNameNamedQuery(String name) {
        Doctor doctor = entityManager.createNamedQuery("doctor.findByName", Doctor.class)
                .setParameter("name", name)
                .getSingleResult();
        return doctor != null ? Optional.of(doctor) : Optional.empty();
    }

    public Optional<Doctor> save(Optional<Doctor> doctor) {

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(doctor);
            entityManager.getTransaction().commit();
            return (Optional<Doctor>) Optional.of(doctor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }



    public boolean remove(Long id) {
        Optional<Doctor> oDoctor = findById(id);
        if (oDoctor.isPresent()) {
            Doctor d = oDoctor.get();

            try {
                entityManager.getTransaction().begin();
                entityManager.remove(d);
                entityManager.getTransaction().commit();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
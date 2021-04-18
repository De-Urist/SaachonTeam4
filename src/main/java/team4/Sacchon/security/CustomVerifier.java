package team4.Sacchon.security;

import org.restlet.Request;
import org.restlet.security.Role;
import org.restlet.security.SecretVerifier;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.ChiefDoctor;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.repository.DoctorRepository;
import team4.Sacchon.repository.PatientRepository;

import javax.persistence.EntityManager;

public class CustomVerifier extends SecretVerifier {

    @Override
    public int verify(String username, char[] password) {
        //check db for user (PatientRepository)
        EntityManager em = JpaUtil.getEntityManager();
        if (checkPatientLogin(em, username, password) == SecretVerifier.RESULT_VALID)
            return SecretVerifier.RESULT_VALID;

        if (checkDoctorLogin(em, username, password) == SecretVerifier.RESULT_VALID)
            return SecretVerifier.RESULT_VALID;

        if (checkChiefLogin(em, username, password)  == SecretVerifier.RESULT_VALID)
            return SecretVerifier.RESULT_VALID;

        return SecretVerifier.RESULT_VALID;
    }

    private int checkPatientLogin(EntityManager em, String username, char[] password) {
        Patient patient = new PatientRepository(em).getByUsername(username);
        if (patient == null)
            return SecretVerifier.RESULT_INVALID;

        String passwordInDb = patient.getPassword();
        if (compare(passwordInDb.toCharArray(), password)) {
            Request.getCurrent().getClientInfo().getRoles().add(new Role(patient.getRole()));
            return SecretVerifier.RESULT_VALID;
        }
        return SecretVerifier.RESULT_INVALID;
    }

    private int checkDoctorLogin(EntityManager em, String username, char[] password) {
        Doctor doctor = new DoctorRepository(em).getByUsername(username);
        if (doctor == null)
            return SecretVerifier.RESULT_INVALID;

        String passwordInDb = doctor.getPassword();
        if (compare(passwordInDb.toCharArray(), password)) {
            Request.getCurrent().getClientInfo().getRoles().add(new Role(doctor.getRole()));
            return SecretVerifier.RESULT_VALID;
        }
        return SecretVerifier.RESULT_INVALID;
    }

    private int checkChiefLogin(EntityManager em, String username, char[] password) {
        ChiefDoctor chiefDoctor = new ChiefDoctorRepository(em).getByUsername(username);
        if (chiefDoctor == null)
            return SecretVerifier.RESULT_INVALID;

        String passwordInDb = chiefDoctor.getPassword();
        if (compare(passwordInDb.toCharArray(), password)) {
            Request.getCurrent().getClientInfo().getRoles().add(new Role(chiefDoctor.getRole()));
            return SecretVerifier.RESULT_VALID;
        }
        return SecretVerifier.RESULT_INVALID;
    }
}

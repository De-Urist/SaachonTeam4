package team4.Sacchon.security;

import org.restlet.Request;
import org.restlet.security.Role;
import org.restlet.security.SecretVerifier;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.ChiefDoctor;
import team4.Sacchon.model.Doctor;
import team4.Sacchon.model.Patient;
import team4.Sacchon.model.User;
import team4.Sacchon.repository.ChiefDoctorRepository;
import team4.Sacchon.repository.DoctorRepository;
import team4.Sacchon.repository.PatientRepository;

import javax.persistence.EntityManager;

public class CustomVerifier extends SecretVerifier {

    @Override
    public int verify(String username, char[] password) {
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
        return checkLogin(password, patient);
    }

    private int checkDoctorLogin(EntityManager em, String username, char[] password) {
        Doctor doctor = new DoctorRepository(em).getByUsername(username);
        return checkLogin(password, doctor);
    }

    private int checkChiefLogin(EntityManager em, String username, char[] password) {
        ChiefDoctor chiefDoctor = new ChiefDoctorRepository(em).getByUsername(username);
        return checkLogin(password, chiefDoctor);
    }

    private int checkLogin(char[] password, User user) {
        if (user != null){
            String passwordInDb = user.getPassword();
            if (compare(passwordInDb.toCharArray(), password)) {
                Request.getCurrent().getClientInfo().getRoles().add(new Role(user.getRole()));
                return SecretVerifier.RESULT_VALID;
            }
        }
        return SecretVerifier.RESULT_INVALID;
    }
}

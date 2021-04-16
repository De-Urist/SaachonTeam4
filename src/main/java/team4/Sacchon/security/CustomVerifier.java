package team4.Sacchon.security;

import org.restlet.security.SecretVerifier;
import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Patient;
import team4.Sacchon.repository.PatientRepository;

import javax.persistence.EntityManager;

public class CustomVerifier extends SecretVerifier {

    @Override
    public int verify(String username, char[] password) {
        //check db for user (PatientRepository)
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.getByUsername(username);
        if(patient == null){
            return SecretVerifier.RESULT_INVALID;
        }
        String passwordInDb = patient.getPassword();
        if(compare(passwordInDb.toCharArray(), password)){
            return SecretVerifier.RESULT_VALID;
        }
        return SecretVerifier.RESULT_INVALID;
    }
}

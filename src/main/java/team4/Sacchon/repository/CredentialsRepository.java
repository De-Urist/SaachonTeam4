package team4.Sacchon.repository;

import team4.Sacchon.model.Credentials;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class CredentialsRepository extends Repository<Credentials, Integer> {

    private EntityManager em;

    public CredentialsRepository(EntityManager em) {
        super(em);
        this.em = em;
    }

    @Override
    public Class<Credentials> getEntityClass() {
        return Credentials.class;
    }

    @Override
    public String getClassName() {
        return Credentials.class.getName();
    }

    public Credentials getByUsername(String username) {
        return em.createQuery("SELECT c from Credentials c WHERE c.username= :username", Credentials.class)
                .setParameter("username", username)
                .getResultList().stream().findFirst().orElse(null);
    }
}
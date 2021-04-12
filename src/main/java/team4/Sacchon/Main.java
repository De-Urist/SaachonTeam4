package team4.Sacchon;

import team4.Sacchon.jpautil.JpaUtil;
import team4.Sacchon.model.Patient;

import javax.persistence.EntityManager;

public class Main {
    public static void main(String[] args) {

        EntityManager em = JpaUtil.getEntityManager();
        System.out.println("Connection established");
        //Entity Manager commands for testing
        em.close();

    }
}

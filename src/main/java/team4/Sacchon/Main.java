package team4.Sacchon;

import team4.Sacchon.jpautil.JpaUtil;

import javax.persistence.EntityManager;

public class Main {
    public static void main(String[] args) {
        /* TESTING
        LocalDate newDate = LocalDate.of(2021, 7, 30);
        LocalDate oldDate = LocalDate.of(2021, 5, 20);
        DateChecker test = new DateChecker();
        boolean test1 = test.compareDates(newDate,oldDate);
        */
        System.out.println("Hello World!");
        EntityManager em = JpaUtil.getEntityManager();
        System.out.println("Connection established");
        em.close();

    }
}

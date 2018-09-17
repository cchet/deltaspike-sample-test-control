package com.gepardec.samples.test.deltaspike.test.control;

import com.gepardec.samples.deltaspike.test.control.jpa.UserEntity;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author Thomas Herzog <thomas.herzog@gepardec.com>
 * @since 9/17/18
 */
@RunWith(CdiTestRunner.class)
public class JPATest {

    private EntityManagerFactory emf;

    @Before
    public void before(){
        emf = Persistence.createEntityManagerFactory("unit");
        final EntityManager em = emf.createEntityManager();
        try{
            final EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(new UserEntity("Thomas", "Herzog", "thomas.herzog@gepardec.com"));
            em.flush();
            et.commit();
        }finally {
            if(em != null) {
                em.clear();
                em.close();
            }
        }
    }

    @After
    public void after(){
        if((emf != null) && (emf.isOpen())) {
            emf.close();
            emf = null;
        }
    }

    @Test
    public void test() {

    }
}

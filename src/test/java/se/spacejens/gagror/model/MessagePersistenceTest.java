package se.spacejens.gagror.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import junit.framework.Assert;

import org.junit.Test;

import se.spacejens.gagror.AbstractTest;

/**
 * Persistence unit tests for {@link Message}.
 * 
 * @author spacejens
 */
public class MessagePersistenceTest extends AbstractTest {

	/**
	 * Store and retrieve a message.
	 * 
	 * @throws Exception
	 *             If unexpected errors occur.
	 */
	@Test
	public void testStoreAndRetrieve() throws Exception {
		final EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("GagrorPersistenceUnit");
		final EntityManager em = emf.createEntityManager();
		final EntityTransaction trans = em.getTransaction();
		trans.begin();
		Message mess1 = new Message("One");
		em.persist(mess1);
		Assert.assertNotNull("Persisted message should have received an ID",
				mess1.getId());
		// TODO
		trans.rollback();
		em.close();
		emf.close();
	}
}

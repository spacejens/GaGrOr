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
		this.log.debug("Creating entity manager factory");
		final EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("GagrorPersistenceUnit");
		this.log.debug("Creating entity manager");
		final EntityManager em = emf.createEntityManager();
		this.log.debug("Starting transaction");
		final EntityTransaction trans = em.getTransaction();
		trans.begin();
		this.log.debug("Doing some work");
		Message mess1 = new Message("One");
		em.persist(mess1);
		Assert.assertNotNull("Persisted message should have received an ID",
				mess1.getId());
		// TODO
		this.log.debug("Rolling back work");
		trans.rollback();
		this.log.debug("Closing entity manager");
		em.close();
		this.log.debug("Closing entity manager factory");
		emf.close();
		this.log.debug("Done closing");
	}
}

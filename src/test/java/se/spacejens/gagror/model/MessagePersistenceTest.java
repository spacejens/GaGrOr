package se.spacejens.gagror.model;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Persistence unit tests for {@link Message}.
 * 
 * @author spacejens
 */
public class MessagePersistenceTest extends PersistenceTestSupport {

	/**
	 * Store and retrieve a message.
	 * 
	 * @throws Exception
	 *             If unexpected errors occur.
	 */
	@Test
	public void testStoreAndRetrieve() throws Exception {
		new TestTransaction() {
			@Override
			public boolean work(final EntityManager em) throws Exception {
				Message mess1 = new Message("One");
				em.persist(mess1);
				Assert.assertNotNull("Persisted message should have received an ID", mess1.getId());
				return false;
			}
		}.run();
	}

	/**
	 * Attempt to store a message with empty text contents.
	 * 
	 * @throws Exception
	 *             If unexpected errors occur.
	 */
	@Test
	public void attemptToStoreEmptyText() throws Exception {
		new TestTransaction() {
			@Override
			public boolean work(final EntityManager em) throws Exception {
				Message mess1 = new Message("");
				try {
					em.persist(mess1);
					Assert.fail("Persisting should fail");
				} catch (final ConstraintViolationException e) {
					// Expected, do nothing
				}
				return false;
			}
		}.run();
	}
}

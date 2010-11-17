package se.spacejens.gagror.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.BeforeClass;
import org.slf4j.Logger;

import se.spacejens.gagror.TestSupport;

/**
 * Provides common functionality for persistence tests.
 * 
 * @author spacejens
 */
public abstract class PersistenceTestSupport extends TestSupport {

	/** Entity manager factory instance to use everywhere. */
	private static EntityManagerFactory entityManagerFactory = null;

	/**
	 * Create an entity manager factory instance for use by all test classes.
	 */
	@BeforeClass
	public static void createEntityManagerFactory() {
		if (null == PersistenceTestSupport.entityManagerFactory) {
			PersistenceTestSupport.entityManagerFactory = Persistence.createEntityManagerFactory("GagrorPersistenceUnit");
			/*
			 * In a real application we would need to close the factory when we
			 * were done with it, but we don't bother in unit tests
			 */
		}
	}

	/**
	 * Create an entity manager instance.
	 * 
	 * @return A newly created instance.
	 */
	protected EntityManager createEntityManager() {
		this.log.debug("Creating entity manager");
		return PersistenceTestSupport.entityManagerFactory.createEntityManager();
	}

	/**
	 * Helper to facilitate running a single, isolated transaction.
	 * 
	 * @author spacejens
	 */
	protected abstract class TestTransaction {
		/** Logger instance to use, same as for test case class. */
		protected final Logger log = PersistenceTestSupport.this.log;

		/**
		 * Run the transaction.
		 * 
		 * @throws Exception
		 *             If thrown by {@link #work()}.
		 */
		public void run() throws Exception {
			final EntityManager em = PersistenceTestSupport.this.createEntityManager();
			this.log.debug("Starting transaction");
			final EntityTransaction trans = em.getTransaction();
			trans.begin();
			this.log.debug("Doing some work");
			try {
				final boolean result = this.work(em);
				if (result) {
					this.log.debug("Committing transaction");
					trans.commit();
				}
			} finally {
				if (trans.isActive()) {
					this.log.debug("Rolling back transaction");
					trans.rollback();
				}
				this.log.debug("Closing entity manager");
				em.close();
			}
		}

		/**
		 * Perform the transaction work. Thrown exceptions will cause the
		 * transaction to be rolled back.
		 * 
		 * @param em
		 *            Entity manager instance to use.
		 * @return true if transaction should be committed, false if it should
		 *         be rolled back.
		 */
		public abstract boolean work(final EntityManager em) throws Exception;
	}
}

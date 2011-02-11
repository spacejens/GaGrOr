package se.spacejens.gagror.model;

import se.spacejens.gagror.TestSupport;
import se.spacejens.gagror.model.user.UserDAO;

/**
 * Superclass for tests of subclasses of {@link DAOClientSupport}, providing
 * shared functionality.
 * 
 * @author spacejens
 */
public abstract class DAOClientSupportTestSupport extends TestSupport {

	/**
	 * Inject a user DAO into an instance.
	 * 
	 * @param instance
	 * @param userDAO
	 */
	protected void injectUserDAO(final DAOClientSupport instance, final UserDAO userDAO) {
		instance.setUserDAO(userDAO);
	}
}

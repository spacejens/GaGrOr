package se.spacejens.gagror.model;

import se.spacejens.gagror.LogAwareSupport;
import se.spacejens.gagror.model.user.UserDAO;
import se.spacejens.gagror.model.user.UserDAOImpl;

/**
 * This class defines common functionality for all objects that use the
 * different types of Data Access Objects. It provides (and reuses) DAO
 * instances, and also allows for manual setting of a DAO instance (which is
 * used for testing purposes).
 * 
 * @author spacejens
 */
public abstract class DAOClientSupport extends LogAwareSupport {

	/** User DAO to use. */
	private UserDAO userDAO = null;

	/**
	 * Get the user DAO to use.
	 * 
	 * @param jpa
	 *            JPA context to use when creating new DAO instance.
	 * @return Not null.
	 */
	protected UserDAO getUserDAO(final JpaContext jpa) {
		if (null == this.userDAO) {
			this.setUserDAO(new UserDAOImpl(jpa));
		}
		return this.userDAO;
	}

	/**
	 * Set the user DAO to use.
	 * 
	 * @param userDAO
	 *            Not null.
	 */
	void setUserDAO(final UserDAO userDAO) {
		this.userDAO = userDAO;
	}
}

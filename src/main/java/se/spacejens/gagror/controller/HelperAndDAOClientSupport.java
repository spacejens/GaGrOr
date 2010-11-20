package se.spacejens.gagror.controller;

import se.spacejens.gagror.controller.helper.UserHelper;
import se.spacejens.gagror.controller.helper.UserHelperImpl;
import se.spacejens.gagror.model.DAOClientSupport;
import se.spacejens.gagror.model.JpaContext;

/**
 * This class defines common functionality for objects that use the various
 * helper and DAO types. It provides and reuses instances, and also allows for
 * manual setting of an instance (which is used for testing purposes).
 * 
 * @author spacejens
 */
public abstract class HelperAndDAOClientSupport extends DAOClientSupport {

	/** User helper to use. */
	private UserHelper userHelper = null;

	/**
	 * Get the user helper to use.
	 * 
	 * @param jpa
	 *            JPA context to use when creating new helper instance.
	 * @return Not null.
	 */
	protected UserHelper getUserHelper(final JpaContext jpa) {
		if (null != this.userHelper) {
			return this.userHelper;
		}
		final UserHelper output = new UserHelperImpl(jpa);
		if (this.isStoringCreatedHelper()) {
			this.setUserHelper(output);
		}
		return output;
	}

	/**
	 * Set the user helper to use.
	 * 
	 * @param userHelper
	 *            Not null.
	 */
	void setUserHelper(final UserHelper userHelper) {
		this.userHelper = userHelper;
	}

	/**
	 * Should created helper objects be stored and reused by later calls to the
	 * same get method?
	 * 
	 * @return true if so.
	 */
	protected abstract boolean isStoringCreatedHelper();
}

package se.spacejens.gagror.controller;

import se.spacejens.gagror.controller.helper.user.EncryptionHelper;
import se.spacejens.gagror.controller.helper.user.EncryptionHelperImpl;
import se.spacejens.gagror.controller.helper.user.UserHelper;
import se.spacejens.gagror.controller.helper.user.UserHelperImpl;
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

	/** Encryption helper to use. */
	private EncryptionHelper encryptionHelper = null;

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
	 * Get the encryption helper to use.
	 * 
	 * @param jpa
	 *            JPA context to use when creating new helper instance.
	 * @return Not null.
	 */
	protected EncryptionHelper getEncryptionHelper(final JpaContext jpa) {
		if (null != this.encryptionHelper) {
			return this.encryptionHelper;
		}
		final EncryptionHelper output = new EncryptionHelperImpl(jpa);
		if (this.isStoringCreatedHelper()) {
			this.setEncryptionHelper(output);
		}
		return output;
	}

	/**
	 * Set the encryption helper to use.
	 * 
	 * @param encryptionHelper
	 *            Not null.
	 */
	void setEncryptionHelper(final EncryptionHelper encryptionHelper) {
		this.encryptionHelper = encryptionHelper;
	}

	/**
	 * Should created helper objects be stored and reused by later calls to the
	 * same get method?
	 * 
	 * @return true if so.
	 */
	protected abstract boolean isStoringCreatedHelper();
}

package se.spacejens.gagror.model.user;

import se.spacejens.gagror.model.Entity;

/**
 * A user in the system.
 * 
 * @author spacejens
 */
public interface UserEntity extends Entity, UserReference, Password {

	/**
	 * Set the encrypted password.
	 * 
	 * @param password
	 *            Not null.
	 */
	public void setPassword(final String password);
}

package se.spacejens.gagror.controller.helper;

import se.spacejens.gagror.model.user.User;

/**
 * Specification for the helper responsible for system user management.
 * 
 * @author spacejens
 */
public interface UserHelper {

	/**
	 * Register a new user. May only be performed if not currently logged in.
	 * 
	 * @param username
	 *            Username of the new user.
	 * @param password
	 *            Plaintext password of the new user.
	 */
	public User registerUser(final String username, final String password);
}

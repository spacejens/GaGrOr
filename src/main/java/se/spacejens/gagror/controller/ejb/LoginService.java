package se.spacejens.gagror.controller.ejb;

import se.spacejens.gagror.GagrorException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.model.user.User;

/**
 * Service used to handle login, logout, and user registration.
 * 
 * @author spacejens
 */
public interface LoginService {

	/**
	 * Register a new user.
	 * 
	 * @param rc
	 *            The request context.
	 * @param username
	 *            Username.
	 * @param password
	 *            Plaintext password.
	 * @return The new user.
	 * @throws GagrorException
	 *             If registration failed.
	 */
	public User registerUser(final RequestContext rc, final String username, final String password) throws GagrorException;
}

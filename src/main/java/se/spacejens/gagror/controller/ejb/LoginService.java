package se.spacejens.gagror.controller.ejb;

import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.controller.ServiceCommunicationException;
import se.spacejens.gagror.controller.helper.RepeatedPasswordNotMatchingException;
import se.spacejens.gagror.model.user.User;
import se.spacejens.gagror.model.user.UserCreationException;

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
	 * @param repeatPassword
	 *            Plaintext password repeated.
	 * @return The new user.
	 * @throws UserCreationException
	 *             If the user could not be created, most likely because the
	 *             username was busy.
	 * @throws ServiceCommunicationException
	 *             If communication with the service failed.
	 * @throws RepeatedPasswordNotMatchingException
	 *             If the repeated password did not match.
	 */
	public User registerUser(final RequestContext rc, final String username, final String password, final String repeatPassword)
			throws UserCreationException, ServiceCommunicationException, RepeatedPasswordNotMatchingException;
}

package se.spacejens.gagror.controller.ejb.core;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.MayNotBeLoggedInException;
import se.spacejens.gagror.controller.NotLoggedInException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.controller.RequestResult;
import se.spacejens.gagror.controller.ServiceCommunicationException;
import se.spacejens.gagror.controller.helper.user.RepeatedPasswordNotMatchingException;
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
	 * @return Not null, logged in as the registered user.
	 * @throws UserCreationException
	 *             If the user could not be created, most likely because the
	 *             username was busy.
	 * @throws ServiceCommunicationException
	 *             If communication with the service failed.
	 * @throws RepeatedPasswordNotMatchingException
	 *             If the repeated password did not match.
	 * @throws MayNotBeLoggedInException
	 *             If a user was logged in.
	 */
	public RequestResult registerUser(final RequestContext rc, final String username, final String password, final String repeatPassword)
			throws UserCreationException, ServiceCommunicationException, RepeatedPasswordNotMatchingException, MayNotBeLoggedInException;

	/**
	 * Log in a user.
	 * 
	 * @param rc
	 *            The request context.
	 * @param username
	 *            Username.
	 * @param password
	 *            Plaintext password.
	 * @return Not null, logged in as the user.
	 * @throws ServiceCommunicationException
	 *             If communication with the service failed.
	 * @throws LoginFailedException
	 *             If login failed.
	 */
	public RequestResult loginUser(final RequestContext rc, final String username, final String password) throws ServiceCommunicationException,
			LoginFailedException;

	/**
	 * Verify that the current user can be logged in.
	 * 
	 * @param rc
	 *            The request context, containing login credentials.
	 * @return Not null, logged in as the user.
	 * @throws ServiceCommunicationException
	 *             If communication with the service failed.
	 * @throws LoginFailedException
	 *             If login failed.
	 * @throws NotLoggedInException
	 *             If the user was not logged in.
	 */
	public RequestResult verifyLogin(final RequestContext rc) throws ServiceCommunicationException, LoginFailedException, NotLoggedInException;

	/**
	 * Log out the current user (if any).
	 * 
	 * @param rc
	 *            The request context.
	 * @return Not null, not logged in as any user,
	 *         {@link LogoutRequestResult#getLoggedOutUser()} is null if no user
	 *         was logged in.
	 * @throws ServiceCommunicationException
	 *             If communication with the service failed.
	 */
	public LogoutRequestResult logoutUser(final RequestContext rc) throws ServiceCommunicationException;
}

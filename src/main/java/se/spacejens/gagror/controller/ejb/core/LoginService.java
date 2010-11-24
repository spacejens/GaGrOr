package se.spacejens.gagror.controller.ejb.core;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.MayNotBeLoggedInException;
import se.spacejens.gagror.controller.NotLoggedInException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.controller.ServiceCommunicationException;
import se.spacejens.gagror.controller.helper.user.RepeatedPasswordNotMatchingException;
import se.spacejens.gagror.model.user.UserEntity;
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
	 * @return The new user, not null.
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
	public UserEntity registerUser(final RequestContext rc, final String username, final String password, final String repeatPassword)
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
	 * @return The logged in user, not null.
	 * @throws ServiceCommunicationException
	 *             If communication with the service failed.
	 * @throws LoginFailedException
	 *             If login failed.
	 */
	public UserEntity loginUser(final RequestContext rc, final String username, final String password) throws ServiceCommunicationException,
			LoginFailedException;

	/**
	 * Verify that the current user can be logged in.
	 * 
	 * @param rc
	 *            The request context, containing login credentials.
	 * @return The logged in user, not null.
	 * @throws ServiceCommunicationException
	 *             If communication with the service failed.
	 * @throws LoginFailedException
	 *             If login failed.
	 * @throws NotLoggedInException
	 *             If the user was not logged in.
	 */
	public UserEntity verifyLogin(final RequestContext rc) throws ServiceCommunicationException, LoginFailedException, NotLoggedInException;

	/**
	 * Log out the current user (if any).
	 * 
	 * @param rc
	 *            The request context.
	 * @return The logged out user, or null if no user was logged in.
	 * @throws ServiceCommunicationException
	 *             If communication with the service failed.
	 */
	public UserEntity logoutUser(final RequestContext rc) throws ServiceCommunicationException;
}

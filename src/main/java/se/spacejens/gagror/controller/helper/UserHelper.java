package se.spacejens.gagror.controller.helper;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.MayNotBeLoggedInException;
import se.spacejens.gagror.model.user.User;
import se.spacejens.gagror.model.user.UserCreationException;

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
	 * @param repeatPassword
	 *            Plaintext password repeated.
	 * @return The created user, not null.
	 * @throws UserCreationException
	 *             If the user could not be created, most likely because the
	 *             username was busy.
	 * @throws RepeatedPasswordNotMatchingException
	 *             If the repeated password did not match.
	 * @throws MayNotBeLoggedInException
	 *             If a user was logged in.
	 */
	public User registerUser(final String username, final String password, final String repeatPassword) throws UserCreationException,
			RepeatedPasswordNotMatchingException, MayNotBeLoggedInException;

	/**
	 * Log in a user.
	 * 
	 * @param username
	 *            Username of the new user.
	 * @param password
	 *            Plaintext password of the new user.
	 * @return The logged in user, not null.
	 * @throws LoginFailedException
	 *             If login failed.
	 */
	public User loginUser(final String username, final String password) throws LoginFailedException;
}

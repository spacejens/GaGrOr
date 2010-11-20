package se.spacejens.gagror.model.user;

/**
 * Specification of functionality for the user DAO.
 * 
 * @author spacejens
 */
public interface UserDAO {

	/**
	 * Create and persist a new user.
	 * 
	 * @param username
	 *            Username to use.
	 * @param password
	 *            Encrypted password to use.
	 * @return The user after persisting.
	 * @throws UserCreationException
	 *             If the user could not be created, most likely because the
	 *             username was busy.
	 */
	public User createUser(final String username, final String password) throws UserCreationException;

	/**
	 * Find user with the specified ID.
	 * 
	 * @param id
	 *            User ID to find.
	 * @return Null if the user could not be found.
	 */
	public User findUser(final Long id);

	/**
	 * Find user with the specified login credentials.
	 * 
	 * @param username
	 *            Username.
	 * @param password
	 *            Encrypted password.
	 * @return Null if the user could not be found (including if the user
	 *         exists, but with another password).
	 */
	public User findUser(final String username, final String password);
}

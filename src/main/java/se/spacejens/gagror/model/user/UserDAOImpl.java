package se.spacejens.gagror.model.user;

import javax.persistence.PersistenceException;

import se.spacejens.gagror.model.DAOSupport;
import se.spacejens.gagror.model.JpaContext;

/**
 * Implementation of user DAO.
 * 
 * @author spacejens
 */
public class UserDAOImpl extends DAOSupport implements UserDAO {

	/**
	 * Create instance.
	 * 
	 * @param jpa
	 *            JPA context to use.
	 */
	public UserDAOImpl(final JpaContext jpa) {
		super(jpa);
	}

	@Override
	public User createUser(final String username, final String password) throws UserCreationException {
		final UserImpl user = new UserImpl();
		user.setUsername(username);
		user.setPassword(password);
		try {
			this.getJpa().getEntityManager().persist(user);
			// Flush to make sure the user can be created
			this.getJpa().getEntityManager().flush();
		} catch (final PersistenceException e) {
			throw new UserCreationException(e);
		}
		return user;
	}
}

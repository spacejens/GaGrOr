package se.spacejens.gagror.model.user;

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
	public User createUser(final String username, final String password) {
		final UserImpl user = new UserImpl();
		user.setUsername(username);
		user.setPassword(password);
		this.getJpa().getEntityManager().persist(user);
		return user;
	}
}

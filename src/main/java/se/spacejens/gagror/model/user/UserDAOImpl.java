package se.spacejens.gagror.model.user;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

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
	public UserEntity createUser(final String username, final String password) throws UserCreationException {
		final UserEntityImpl user = new UserEntityImpl();
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

	@Override
	public UserEntity findUser(final Long id) {
		return this.getJpa().getEntityManager().find(UserEntityImpl.class, id);
	}

	@Override
	public UserEntity findUser(final String username, final String password) {
		final TypedQuery<UserEntityImpl> query = this.getJpa().getEntityManager().createNamedQuery("UserEntityImpl.login", UserEntityImpl.class);
		query.setParameter("username", username);
		query.setParameter("password", password);
		try {
			return query.getSingleResult();
		} catch (final NoResultException e) {
			return null;
		}
	}
}

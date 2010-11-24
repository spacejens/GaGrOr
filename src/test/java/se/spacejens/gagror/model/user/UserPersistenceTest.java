package se.spacejens.gagror.model.user;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;

import se.spacejens.gagror.model.PersistenceTestSupport;

/**
 * Persistence unit tests for {@link UserEntity}.
 * 
 * @author spacejens
 */
public class UserPersistenceTest extends PersistenceTestSupport {

	/**
	 * Store an instance in the database and retrieve it again.
	 * 
	 * @throws Exception
	 *             If unexpected errors occur.
	 */
	@Test
	public void storeAndRetrieve() throws Exception {
		final UserEntityImpl user = new UserEntityImpl();
		user.setUsername("top");
		user.setPassword("fgkjdlfgg0khgfjievw0gfdryth0");
		new TestTransaction() {
			@Override
			public boolean work(final EntityManager em) throws Exception {
				em.persist(user);
				Assert.assertNotNull("No ID was assigned when persisting", user.getId());
				Assert.assertNotNull("No modified timestamp was assigned when persisting", user.getModificationTimestamp());
				return true;
			}
		}.run();
		new TestTransaction() {
			@Override
			public boolean work(final EntityManager em) throws Exception {
				final UserEntityImpl retrieved = em.find(UserEntityImpl.class, user.getId());
				Assert.assertEquals("Wrong ID", user.getId(), retrieved.getId());
				Assert.assertEquals("Wrong username", user.getUsername(), retrieved.getUsername());
				Assert.assertEquals("Wrong password", user.getPassword(), retrieved.getPassword());
				return false;
			}
		}.run();
		new TestTransaction() {
			@Override
			public boolean work(final EntityManager em) throws Exception {
				final TypedQuery<UserEntityImpl> query = em.createNamedQuery("UserEntityImpl.login", UserEntityImpl.class);
				query.setParameter("username", user.getUsername());
				query.setParameter("password", user.getPassword());
				final UserEntityImpl retrieved = query.getSingleResult();
				Assert.assertEquals("Wrong ID", user.getId(), retrieved.getId());
				Assert.assertEquals("Wrong username", user.getUsername(), retrieved.getUsername());
				Assert.assertEquals("Wrong password", user.getPassword(), retrieved.getPassword());
				return false;
			}
		}.run();
	}
}

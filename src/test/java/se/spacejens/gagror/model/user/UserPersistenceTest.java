package se.spacejens.gagror.model.user;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;

import se.spacejens.gagror.model.PersistenceTestSupport;

/**
 * Persistence unit tests for {@link User}.
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
		final UserImpl user = new UserImpl();
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
				final UserImpl retrieved = em.find(UserImpl.class, user.getId());
				Assert.assertEquals("Wrong ID", user.getId(), retrieved.getId());
				Assert.assertEquals("Wrong username", user.getUsername(), retrieved.getUsername());
				Assert.assertEquals("Wrong password", user.getPassword(), retrieved.getPassword());
				return false;
			}
		}.run();
		new TestTransaction() {
			@Override
			public boolean work(final EntityManager em) throws Exception {
				final TypedQuery<UserImpl> query = em.createNamedQuery("UserImpl.login", UserImpl.class);
				query.setParameter("username", user.getUsername());
				query.setParameter("password", user.getPassword());
				final UserImpl retrieved = query.getSingleResult();
				Assert.assertEquals("Wrong ID", user.getId(), retrieved.getId());
				Assert.assertEquals("Wrong username", user.getUsername(), retrieved.getUsername());
				Assert.assertEquals("Wrong password", user.getPassword(), retrieved.getPassword());
				return false;
			}
		}.run();
	}
}

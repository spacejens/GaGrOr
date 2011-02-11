package se.spacejens.gagror.controller;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import se.spacejens.gagror.SecurityConstants;
import se.spacejens.gagror.model.DAOClientSupportTestSupport;
import se.spacejens.gagror.model.JpaContext;
import se.spacejens.gagror.model.user.UserDAO;
import se.spacejens.gagror.model.user.UserEntity;

/**
 * Unit test for {@link ControllerSupport}.
 * 
 * @author spacejens
 */
public class ControllerSupportTest extends DAOClientSupportTestSupport {

	/**
	 * Positive test (logged in) of
	 * {@link ControllerSupport#getJpaContext(RequestContext, javax.persistence.EntityManager)}
	 * 
	 * @throws Exception
	 *             If unexpected errors occur.
	 */
	@Test
	public void testGetJpaContext() throws Exception {
		// Test object instance
		final ControllerSupport instance = new ControllerSupportImpl();
		// Create data to use as parameters to the method
		final EntityManager ent = Mockito.mock(EntityManager.class);
		final RequestContext rc = Mockito.mock(RequestContext.class);
		final String username = "kalle";
		final String password = "topsecret";
		Mockito.when(rc.getUsername()).thenReturn(username);
		Mockito.when(rc.getPassword()).thenReturn(password);
		Mockito.when(rc.isContainingLoginInformation()).thenReturn(Boolean.TRUE);
		// Insert a user DAO
		final UserEntity user = Mockito.mock(UserEntity.class);
		Mockito.when(user.getUsername()).thenReturn(username);
		Mockito.when(user.getPassword()).thenReturn(password);
		final UserDAO userDAO = Mockito.mock(UserDAO.class);
		Mockito.when(userDAO.findUser(Matchers.anyString(), Matchers.anyString())).thenReturn(user);
		this.injectUserDAO(instance, userDAO);
		// Call the test method and verify the results
		final JpaContext jpa = instance.getJpaContext(rc, ent);
		Assert.assertTrue("Unexpected entity manager", jpa.getEntityManager() == ent);
		Assert.assertTrue("Should have been logged in", jpa.isContainingLoginInformation());
		Assert.assertTrue("Unexpected logged in user", jpa.getCurrentUser() == user);
	}

	/**
	 * Positive test (not logged in) of
	 * {@link ControllerSupport#getJpaContext(RequestContext, javax.persistence.EntityManager)}
	 * 
	 * @throws Exception
	 *             If unexpected errors occur.
	 */
	@Test
	public void testGetJpaContextNotLoggedIn() throws Exception {
		// TODO
	}

	/**
	 * Negative test (login failed) of
	 * {@link ControllerSupport#getJpaContext(RequestContext, javax.persistence.EntityManager)}
	 */
	@Test
	public void testGetJpaContextLoginFailed() {
		// TODO
	}

	private void performGetJpaContextTest(final String username, final String password, final SecurityConstants user) {
		// TODO
	}

	/**
	 * Minimal implementation of abstract test object class.
	 * 
	 * @author spacejens
	 */
	private class ControllerSupportImpl extends ControllerSupport {

		@Override
		protected boolean isStoringCreatedHelper() {
			return false;
		}

		@Override
		protected boolean isStoringCreatedDAO() {
			return false;
		}
	}
}

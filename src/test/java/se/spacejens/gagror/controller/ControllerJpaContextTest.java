package se.spacejens.gagror.controller;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import se.spacejens.gagror.TestSupport;
import se.spacejens.gagror.model.user.UserEntity;

/**
 * Unit test of {@link ControllerJpaContext}.
 * 
 * @author spacejens
 */
public class ControllerJpaContextTest extends TestSupport {

	/**
	 * Create an instance and verify that it contains the correct objects.
	 */
	@Test
	public void verifyContainedObjects() {
		// Create instance
		final UserEntity user = Mockito.mock(UserEntity.class);
		final EntityManager ent = Mockito.mock(EntityManager.class);
		final ControllerJpaContext jpa = new ControllerJpaContext(ent);
		// Verify behavior
		Assert.assertFalse("No login info added yet", jpa.isContainingLoginInformation());
		jpa.setCurrentUser(user);
		Assert.assertTrue("Unexpected user", jpa.getCurrentUser() == user);
		Assert.assertTrue("Unexpected entity manager", jpa.getEntityManager() == ent);
		Assert.assertTrue("Login info was added, unexpected response", jpa.isContainingLoginInformation());
	}
}

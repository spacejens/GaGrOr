package se.spacejens.gagror.controller;

import javax.persistence.EntityManager;

import se.spacejens.gagror.model.JpaContext;
import se.spacejens.gagror.model.user.User;

/**
 * Superclass for all controller (i.e. not helper) implementations, providing
 * shared functionality.
 * 
 * @author spacejens
 */
public abstract class ControllerSupport extends HelperAndDAOClientSupport {

	/**
	 * Get the persistence context for a specific request context.
	 * 
	 * @param requestContext
	 *            Not null.
	 * @param entityManager
	 *            Not null.
	 * @return Not null.
	 * @throws LoginFailedException
	 *             If login using the credentials in the request context fails.
	 */
	protected JpaContext getJpaContext(final RequestContext requestContext, final EntityManager entityManager) throws LoginFailedException {
		final ControllerJpaContext jpa = new ControllerJpaContext(entityManager);
		final User currentUser = this.getUserDAO(jpa).findUser(requestContext.getUsername(), requestContext.getPassword());
		jpa.setCurrentUser(currentUser);
		if (null == currentUser) {
			this.getLog().debug("Created controller JPA context, no user logged in");
		} else {
			this.getLog().debug("Created controller JPA context, logged in user is {}", currentUser.getUsername());
		}
		if (requestContext.isContainingLoginInformation() && !jpa.isContainingLoginInformation()) {
			throw new LoginFailedException();
		}
		return jpa;
	}
}

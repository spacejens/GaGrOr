package se.spacejens.gagror.controller;

import javax.persistence.EntityManager;

import se.spacejens.gagror.model.JpaContext;

/**
 * Superclass for all controller (i.e. not helper) implementations, providing
 * shared functionality.
 * 
 * @author spacejens
 */
public class ControllerSupport extends HelperAndDAOClientSupport {

	/**
	 * Get the persistence context for a specific request context.
	 * 
	 * @param requestContext
	 *            Not null.
	 * @param entityManager
	 *            Not null.
	 * @return Not null.
	 */
	protected JpaContext getJpaContext(final RequestContext requestContext, final EntityManager entityManager) {
		final JpaContext jpa = new ControllerJpaContext(entityManager);
		// TODO Fill JPA context with information
		return jpa;
	}
}

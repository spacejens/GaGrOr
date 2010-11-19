package se.spacejens.gagror.controller;

import javax.persistence.EntityManager;

import se.spacejens.gagror.model.JpaContext;

/**
 * Implementation of persistence context information container.
 * 
 * @author spacejens
 */
class ControllerJpaContext implements JpaContext {

	/** Entity manager instance. */
	private final EntityManager entityManager;

	/**
	 * Create instance.
	 * 
	 * @param entityManager
	 *            Not null.
	 */
	public ControllerJpaContext(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public EntityManager getEntityManager() {
		return this.entityManager;
	}
}

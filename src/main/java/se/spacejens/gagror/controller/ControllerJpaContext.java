package se.spacejens.gagror.controller;

import javax.persistence.EntityManager;

import se.spacejens.gagror.model.JpaContext;
import se.spacejens.gagror.model.user.UserEntity;

/**
 * Implementation of persistence context information container.
 * 
 * @author spacejens
 */
class ControllerJpaContext implements JpaContext {

	/** Entity manager instance. */
	private final EntityManager entityManager;

	/** Currently logged in user, or null. */
	private UserEntity currentUser = null;

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

	@Override
	public UserEntity getCurrentUser() {
		return this.currentUser;
	}

	/**
	 * Set the currently logged in user. Called directly after creation by
	 * {@link ControllerSupport#getJpaContext(RequestContext, EntityManager)}.
	 * 
	 * @param currentUser
	 *            Null if not logged in.
	 */
	void setCurrentUser(final UserEntity currentUser) {
		this.currentUser = currentUser;
	}

	@Override
	public boolean isContainingLoginInformation() {
		return this.getCurrentUser() != null;
	}
}

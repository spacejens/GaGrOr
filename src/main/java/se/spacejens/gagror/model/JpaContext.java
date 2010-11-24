package se.spacejens.gagror.model;

import javax.persistence.EntityManager;

import se.spacejens.gagror.model.user.UserEntity;

/**
 * Persistence context information, including entity manager.
 * 
 * @author spacejens
 */
public interface JpaContext {

	/**
	 * Get entity manager instance used in this persistence context.
	 * 
	 * @return Not null.
	 */
	public EntityManager getEntityManager();

	/**
	 * Get the currently logged in user.
	 * 
	 * @return Null if no user is logged in.
	 */
	public UserEntity getCurrentUser();

	/**
	 * Check if the context contains a logged in user or any other login
	 * information.
	 * 
	 * @return true if any such information is present.
	 */
	public boolean isContainingLoginInformation();
}

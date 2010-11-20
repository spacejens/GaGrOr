package se.spacejens.gagror.model;

import javax.persistence.EntityManager;

import se.spacejens.gagror.model.user.User;

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
	public User getCurrentUser();
}

package se.spacejens.gagror.model;

import javax.persistence.EntityManager;

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
}

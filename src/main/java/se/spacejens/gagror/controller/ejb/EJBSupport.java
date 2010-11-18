package se.spacejens.gagror.controller.ejb;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import se.spacejens.gagror.controller.HelperAndDAOClientSupport;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.model.JpaContext;

/**
 * Superclass for all EJB implementations, providing shared functionality.
 * 
 * @author spacejens
 */
public abstract class EJBSupport extends HelperAndDAOClientSupport {

	/** Entity manager instance to use. */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Get the persistence context for a specific view layer request context.
	 * 
	 * @param requestContext
	 *            Not null.
	 * @return Not null.
	 */
	protected JpaContext getJpaContext(final RequestContext requestContext) {
		return new JpaContextImpl(this.entityManager);
	}
}

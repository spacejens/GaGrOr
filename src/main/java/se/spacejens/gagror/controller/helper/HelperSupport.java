package se.spacejens.gagror.controller.helper;

import se.spacejens.gagror.controller.HelperAndDAOClientSupport;
import se.spacejens.gagror.model.JpaContext;

/**
 * Superclass for all helpers, providing shared functionality.
 * 
 * @author spacejens
 */
public abstract class HelperSupport extends HelperAndDAOClientSupport {

	/** JPA context information to use. */
	private final JpaContext jpa;

	/**
	 * Create an instance.
	 * 
	 * @param jpa
	 *            The JPA context information to use.
	 */
	protected HelperSupport(final JpaContext jpa) {
		this.jpa = jpa;
	}

	/**
	 * Get JPA context information to use.
	 * 
	 * @return Not null.
	 */
	protected JpaContext getJpa() {
		return this.jpa;
	}
}

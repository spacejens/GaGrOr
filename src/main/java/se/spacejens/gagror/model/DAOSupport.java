package se.spacejens.gagror.model;

/**
 * Superclass of all Data Access Objects, providing shared functionality.
 * 
 * @author spacejens
 */
public abstract class DAOSupport extends DAOClientSupport {

	/** JPA context information to use. */
	private final JpaContext jpa;

	/**
	 * Create an instance.
	 * 
	 * @param jpa
	 *            The JPA context information to use.
	 */
	protected DAOSupport(final JpaContext jpa) {
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

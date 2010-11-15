package se.spacejens.gagror.controller.ejb;

import javax.naming.NamingException;

import se.spacejens.gagror.LogAwareSupport;
import se.spacejens.gagror.controller.NamingContextProvider;

/**
 * Superclass for all EJB client implementations, providing shared
 * functionality.
 * 
 * @author spacejens
 * @param <L>
 *            Bean interface type.
 */
public abstract class EJBClientSupport<L> extends LogAwareSupport {

	/** Instance to use to access naming context for lookups. */
	private final NamingContextProvider namingContextProvider;

	/**
	 * Create client instance
	 * 
	 * @param namingContextProvider
	 *            Used for JNDI lookup.
	 */
	protected EJBClientSupport(final NamingContextProvider namingContextProvider) {
		this.namingContextProvider = namingContextProvider;
	}

	/**
	 * Get a reference to the EJB.
	 * 
	 * @return Never null.
	 * @throws EJBLookupException
	 *             If EJB lookup failed.
	 */
	@SuppressWarnings("unchecked")
	protected L getReference() throws EJBLookupException {
		try {
			return (L) this.namingContextProvider.getContext().lookup(
					"java:module/" + this.getBeanName());
		} catch (final NamingException e) {
			throw new EJBLookupException(e);
		}
	}

	/**
	 * Get the bean name.
	 * 
	 * @return The unqualified (i.e. without package) class name of the bean
	 *         implementation.
	 */
	protected abstract String getBeanName();
}

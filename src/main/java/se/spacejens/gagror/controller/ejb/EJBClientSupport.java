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

	/**
	 * JNDI prefix for locating EJBs. Append {@link #getBeanName()} to complete.
	 */
	static final String JNDI_PREFIX_EJB = "java:module/";

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
	protected L getReference() throws EJBCommunicationException {
		try {
			return (L) this.namingContextProvider.getContext().lookup(EJBClientSupport.JNDI_PREFIX_EJB + this.getBeanName());
		} catch (final NamingException e) {
			throw new EJBCommunicationException(e);
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

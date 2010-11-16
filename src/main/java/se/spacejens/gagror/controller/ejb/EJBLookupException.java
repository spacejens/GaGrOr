package se.spacejens.gagror.controller.ejb;

import se.spacejens.gagror.GagrorException;

/**
 * EJB lookup failed.
 * 
 * @author spacejens
 */
public class EJBLookupException extends GagrorException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public EJBLookupException() {
	}

	public EJBLookupException(final String message) {
		super(message);
	}

	public EJBLookupException(final Throwable cause) {
		super(cause);
	}

	public EJBLookupException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

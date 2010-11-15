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

	public EJBLookupException(final String arg0) {
		super(arg0);
	}

	public EJBLookupException(final Throwable arg0) {
		super(arg0);
	}

	public EJBLookupException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}
}

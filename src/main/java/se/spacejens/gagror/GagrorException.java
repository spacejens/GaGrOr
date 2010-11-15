package se.spacejens.gagror;

/**
 * A generic system exception. Subclasses are more specific.
 * 
 * @author spacejens
 */
public class GagrorException extends Exception {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public GagrorException() {
	}

	public GagrorException(final String arg0) {
		super(arg0);
	}

	public GagrorException(final Throwable arg0) {
		super(arg0);
	}

	public GagrorException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}
}

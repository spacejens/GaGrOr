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

	public GagrorException(final String message) {
		super(message);
	}

	public GagrorException(final Throwable cause) {
		super(cause);
	}

	public GagrorException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

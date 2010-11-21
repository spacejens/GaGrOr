package se.spacejens.gagror.controller;

/**
 * Thrown when a user should have been logged in, but wasn't.
 * 
 * @author spacejens
 */
public class NotLoggedInException extends ControllerException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public NotLoggedInException() {
	}

	public NotLoggedInException(final String message) {
		super(message);
	}

	public NotLoggedInException(final Throwable cause) {
		super(cause);
	}

	public NotLoggedInException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

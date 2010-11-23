package se.spacejens.gagror.controller;

/**
 * Exception thrown when login fails.
 * 
 * @author spacejens
 */
public class LoginFailedException extends ControllerException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public LoginFailedException() {
		super();
	}

	public LoginFailedException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public LoginFailedException(final String message) {
		super(message);
	}

	public LoginFailedException(final Throwable cause) {
		super(cause);
	}
}

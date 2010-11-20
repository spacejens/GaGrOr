package se.spacejens.gagror.controller;

/**
 * Thrown when a logged in user attempts to perform an action that requires that
 * no user is logged in.
 * 
 * @author spacejens
 */
public class MayNotBeLoggedInException extends ControllerException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public MayNotBeLoggedInException() {
	}

	public MayNotBeLoggedInException(final String message) {
		super(message);
	}

	public MayNotBeLoggedInException(final Throwable cause) {
		super(cause);
	}

	public MayNotBeLoggedInException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

package se.spacejens.gagror.controller.helper;

/**
 * Thrown when a repeated password (e.g. at user registration or password
 * change) does not match.
 * 
 * @author spacejens
 */
public class RepeatedPasswordNotMatchingException extends HelperLogicException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public RepeatedPasswordNotMatchingException() {
	}

	public RepeatedPasswordNotMatchingException(final String message) {
		super(message);
	}

	public RepeatedPasswordNotMatchingException(final Throwable cause) {
		super(cause);
	}

	public RepeatedPasswordNotMatchingException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

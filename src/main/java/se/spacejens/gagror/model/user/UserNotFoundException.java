package se.spacejens.gagror.model.user;

import se.spacejens.gagror.model.EntityNotFoundException;

/**
 * Thrown when the requested user could not be found.
 * 
 * @author spacejens
 */
public class UserNotFoundException extends EntityNotFoundException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException() {
	}

	public UserNotFoundException(final String message) {
		super(message);
	}

	public UserNotFoundException(final Throwable cause) {
		super(cause);
	}

	public UserNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

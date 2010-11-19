package se.spacejens.gagror.model.user;

import se.spacejens.gagror.model.ModelException;

/**
 * Thrown when a user could not be created.
 * 
 * @author spacejens
 */
public class UserCreationException extends ModelException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public UserCreationException() {
	}

	public UserCreationException(final String message) {
		super(message);
	}

	public UserCreationException(final Throwable cause) {
		super(cause);
	}

	public UserCreationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

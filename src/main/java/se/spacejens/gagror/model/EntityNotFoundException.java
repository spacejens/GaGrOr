package se.spacejens.gagror.model;

/**
 * Thrown when a requested entity did not exist.
 * 
 * @author spacejens
 */
public class EntityNotFoundException extends ModelException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException() {
	}

	public EntityNotFoundException(final String message) {
		super(message);
	}

	public EntityNotFoundException(final Throwable cause) {
		super(cause);
	}

	public EntityNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

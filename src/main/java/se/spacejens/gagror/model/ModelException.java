package se.spacejens.gagror.model;

import se.spacejens.gagror.GagrorException;

/**
 * Thrown when there are problems in the model layer.
 * 
 * @author spacejens
 */
public class ModelException extends GagrorException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public ModelException() {
	}

	public ModelException(final String message) {
		super(message);
	}

	public ModelException(final Throwable cause) {
		super(cause);
	}

	public ModelException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

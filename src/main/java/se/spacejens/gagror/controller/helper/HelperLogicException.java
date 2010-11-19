package se.spacejens.gagror.controller.helper;

import se.spacejens.gagror.controller.ControllerException;

/**
 * Thrown by helper logic for business domain (i.e. various data and relation
 * rules) errors.
 * 
 * @author spacejens
 */
public class HelperLogicException extends ControllerException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public HelperLogicException() {
	}

	public HelperLogicException(final String message) {
		super(message);
	}

	public HelperLogicException(final Throwable cause) {
		super(cause);
	}

	public HelperLogicException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

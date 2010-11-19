package se.spacejens.gagror.controller;

import se.spacejens.gagror.GagrorException;

/**
 * Thrown when there are problems in the controller layer.
 * 
 * @author spacejens
 */
public class ControllerException extends GagrorException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public ControllerException() {
	}

	public ControllerException(final String message) {
		super(message);
	}

	public ControllerException(final Throwable cause) {
		super(cause);
	}

	public ControllerException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

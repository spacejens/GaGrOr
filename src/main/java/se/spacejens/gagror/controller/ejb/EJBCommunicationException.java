package se.spacejens.gagror.controller.ejb;

import se.spacejens.gagror.controller.ServiceCommunicationException;

/**
 * Thrown when there are problems communicating with an EJB.
 * 
 * @author spacejens
 */
public class EJBCommunicationException extends ServiceCommunicationException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public EJBCommunicationException() {
	}

	public EJBCommunicationException(final String message) {
		super(message);
	}

	public EJBCommunicationException(final Throwable cause) {
		super(cause);
	}

	public EJBCommunicationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

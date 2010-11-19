package se.spacejens.gagror.controller;

/**
 * Thrown when there are problems communicating with one of the services. This
 * is logged as an error.
 * 
 * @author spacejens
 */
public class ServiceCommunicationException extends ControllerException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public ServiceCommunicationException() {
		this.getLog().error("Service communication problems", this);
	}

	public ServiceCommunicationException(final String message) {
		super(message);
		this.getLog().error("Service communication problems", this);
	}

	public ServiceCommunicationException(final Throwable cause) {
		super(cause);
		this.getLog().error("Service communication problems", this);
	}

	public ServiceCommunicationException(final String message, final Throwable cause) {
		super(message, cause);
		this.getLog().error("Service communication problems", this);
	}
}

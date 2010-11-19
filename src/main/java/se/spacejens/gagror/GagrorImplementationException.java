package se.spacejens.gagror;

/**
 * Thrown when the Gagror implementation breaks internal contracts or otherways
 * behaves in unexpected ways. This is always an error, and will be logged as
 * such.
 * 
 * @author spacejens
 */
public class GagrorImplementationException extends GagrorRuntimeException {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	public GagrorImplementationException() {
		this.getLog().error("Implementation error", this);
	}

	public GagrorImplementationException(final String message) {
		super(message);
		this.getLog().error("Implementation error", this);
	}

	public GagrorImplementationException(final Throwable cause) {
		super(cause);
		this.getLog().error("Implementation error", this);
	}

	public GagrorImplementationException(final String message, final Throwable cause) {
		super(message, cause);
		this.getLog().error("Implementation error", this);
	}
}

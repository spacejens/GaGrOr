package se.spacejens.gagror;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thrown for errors that do not need to be explicitly caught or thrown in the
 * source code.
 * 
 * @author spacejens
 */
public class GagrorRuntimeException extends RuntimeException implements LogAware {

	/** Required by {@link java.io.Serializable} interface. */
	private static final long serialVersionUID = 1L;

	/** Logger to use for this object. */
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public GagrorRuntimeException() {
		this.getLog().debug("Creating instance");
	}

	public GagrorRuntimeException(final String message) {
		super(message);
		this.getLog().debug("Creating instance with message");
	}

	public GagrorRuntimeException(final Throwable cause) {
		super(cause);
		this.getLog().debug("Creating instance with cause");
	}

	public GagrorRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
		this.getLog().debug("Creating instance with message and cause");
	}

	@Override
	public Logger getLog() {
		return this.log;
	}
}

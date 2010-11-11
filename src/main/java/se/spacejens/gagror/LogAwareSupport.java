package se.spacejens.gagror;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience superclass for logging objects.
 * 
 * @author spacejens
 */
public abstract class LogAwareSupport implements LogAware {

	/** Logger to use for this object. */
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public Logger getLog() {
		return this.log;
	}
}

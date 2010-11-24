package se.spacejens.gagror;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Superclass of all tests, providing some shared functionality.
 * 
 * @author spacejens
 */
public abstract class TestSupport {

	/** A logger instance to use for debug printouts. */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Build a alphabetical string of the specified length.
	 * 
	 * @param length
	 *            The length of the string.
	 * @return The generated string.
	 */
	protected String buildString(final int length) {
		final StringBuffer output = new StringBuffer();
		for (int i = 0; i < length; i++) {
			output.append("x");
		}
		return output.toString();
	}
}

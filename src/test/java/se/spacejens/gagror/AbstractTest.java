package se.spacejens.gagror;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Superclass of all tests, providing some shared functionality.
 * 
 * @author spacejens
 */
public abstract class AbstractTest {

	/** A logger instance to use for debug printouts. */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
}

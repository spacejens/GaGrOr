package se.spacejens.gagror;

import org.slf4j.Logger;

/**
 * Interface for all logging classes.
 * 
 * @author spacejens
 */
public interface LogAware {

	/**
	 * Get the logger used by this object when logging.
	 * 
	 * @return Never null.
	 */
	Logger getLog();
}

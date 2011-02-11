package se.spacejens.gagror.model;

/**
 * Interface for objects that are identified by an ID.
 * 
 * @author spacejens
 */
public interface Identified {

	/**
	 * Get the ID.
	 * 
	 * @return Null if not yet persistent.
	 */
	public Long getId();
}

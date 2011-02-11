package se.spacejens.gagror.model;

import java.sql.Timestamp;

/**
 * Interface for objects that are timestamped when created.
 * 
 * @author spacejens
 */
public interface TimestampCreation {

	/**
	 * When was the entity created?
	 * 
	 * @return Not null.
	 */
	public Timestamp getCreationTimestamp();
}

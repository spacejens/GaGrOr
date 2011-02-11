package se.spacejens.gagror.model;

import java.sql.Timestamp;

/**
 * Interface for objects that are timestamped when modified.
 * 
 * @author spacejens
 */
public interface TimestampModification {

	/**
	 * When was the entity last modified?
	 * 
	 * @return Not null.
	 */
	public Timestamp getModificationTimestamp();
}

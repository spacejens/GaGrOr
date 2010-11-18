package se.spacejens.gagror.model;

import java.sql.Timestamp;

/**
 * Interface implemented by all entities, defining common operations.
 * 
 * @author spacejens
 */
public interface Entity {

	/**
	 * Get the ID.
	 * 
	 * @return Null if not yet persistent.
	 */
	Long getId();

	/**
	 * When was the entity created?
	 * 
	 * @return Not null.
	 */
	Timestamp getCreationTimestamp();

	/**
	 * When was the entity last modified?
	 * 
	 * @return Not null.
	 */
	Timestamp getModificationTimestamp();
}

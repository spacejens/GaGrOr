package se.spacejens.gagror.model;

import java.sql.Timestamp;

import se.spacejens.gagror.LogAwareSupport;

/**
 * Superclass for all DTOs, providing shared functionality.
 * 
 * @author spacejens
 */
public abstract class DTOImpl extends LogAwareSupport implements DTO {

	/** Database primary key value. */
	private final Long id;

	/** Time of original entity creation. */
	private final Timestamp creationTimestamp;

	/** Time of latest entity modification. */
	private final Timestamp modificationTimestamp;

	/**
	 * Create DTO instance from content of entity.
	 * 
	 * @param entity
	 */
	protected DTOImpl(final EntityImpl entity) {
		this.id = entity.getId();
		this.creationTimestamp = entity.getCreationTimestamp();
		this.modificationTimestamp = entity.getModificationTimestamp();
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public Timestamp getCreationTimestamp() {
		return this.creationTimestamp;
	}

	@Override
	public Timestamp getModificationTimestamp() {
		return this.modificationTimestamp;
	}
}

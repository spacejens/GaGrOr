package se.spacejens.gagror.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import se.spacejens.gagror.LogAwareSupport;

/**
 * Superclass for all entities, providing common functionality.
 * 
 * @author spacejens
 */
@MappedSuperclass
public abstract class EntitySupport extends LogAwareSupport implements Entity {

	/** Database primary key value. */
	private Long id;

	/** Time of original entity creation. */
	private Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());

	/** Time of latest entity modification. */
	private Timestamp modificationTimestamp;

	@Override
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the ID. Should only be called by the persistence layer.
	 * 
	 * @param id
	 *            Not null.
	 */
	void setId(final Long id) {
		this.getLog().debug("Changing ID from {} to {}", this.id, id);
		this.id = id;
	}

	@Override
	@NotNull
	@Column(name = "creationtimestamp", insertable = true, updatable = false)
	public Timestamp getCreationTimestamp() {
		return this.creationTimestamp;
	}

	/**
	 * Change the creation timestamp. Should only be called by persistence
	 * framework.
	 * 
	 * @param creationTimestamp
	 *            New timestamp.
	 */
	void setCreationTimestamp(final Timestamp creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	@Override
	@Version
	@Column(name = "modificationtimestamp")
	public Timestamp getModificationTimestamp() {
		return this.modificationTimestamp;
	}

	/**
	 * Change the modification timestamp. Should only be called by persistence
	 * framework.
	 * 
	 * @param modificationTimestamp
	 *            New timestamp.
	 */
	void setModificationTimestamp(final Timestamp modificationTimestamp) {
		this.modificationTimestamp = modificationTimestamp;
	}
}

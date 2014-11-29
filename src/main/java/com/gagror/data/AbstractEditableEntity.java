package com.gagror.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractEditableEntity extends AbstractEntity {

	@Version
	@Getter
	private Long version;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	private Date modified;

	@Override
	public String toString() {
		return String.format("%s, version=%d", super.toString(), getVersion());
	}

	@PrePersist
	@PreUpdate
	private void updateModified() {
		modified = new Date();
	}
}

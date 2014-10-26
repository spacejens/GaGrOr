package com.gagror.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of={}, callSuper=true)
@MappedSuperclass
public abstract class AbstractEditableEntity extends AbstractEntity {

	@Version
	private Long version;

	@Column(nullable = false, insertable = true, updatable = true)
	private Date modified;

	@PrePersist
	@PreUpdate
	private void updateModified() {
		modified = new Date();
	}
}

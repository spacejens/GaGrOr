package com.gagror.data;

import javax.persistence.MappedSuperclass;
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

	// TODO Move modified timestamp here, as all editable entities should have it

	// TODO Auto-set the modified timestamp with a @PrePersist, @PreUpdate callback
}

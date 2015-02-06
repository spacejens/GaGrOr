package com.gagror.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity
extends AbstractIdentifiable {

	@Id
	@GeneratedValue
	@Getter
	private Long id;

	@Column(nullable = false, insertable = true, updatable = false)
	@Getter
	private Date created;

	// TODO Forbid deletion of entities, instead using an active flag to track if they have been deleted (soft delete)

	@PrePersist
	private void initializeCreated() {
		if(null == created) {
			created = new Date();
		}
	}
}

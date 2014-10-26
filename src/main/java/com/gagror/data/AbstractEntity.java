package com.gagror.data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of="id")
@MappedSuperclass
public abstract class AbstractEntity implements Identifiable<Long> {

	@Id
	@GeneratedValue
	private Long id;

	// TODO Move created timestamp here, as all entities should have it

	// TODO Auto-set the created timestamp in a @PrePersist callback
}

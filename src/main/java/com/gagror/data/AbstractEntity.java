package com.gagror.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString // TODO Provide a final implementation of toString for all entities
@NoArgsConstructor
@EqualsAndHashCode(of="id")
@MappedSuperclass
public abstract class AbstractEntity implements Identifiable<Long> {

	@Id
	@GeneratedValue
	@Getter
	private Long id;

	@Column(nullable = false, insertable = true, updatable = false)
	@Getter
	private Date created;

	@PrePersist
	private void initializeCreated() {
		if(null == created) {
			created = new Date();
		}
	}
}

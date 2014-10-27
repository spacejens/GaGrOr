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

	@Override
	public String toString() {
		return String.format("id=%d", getId());
	}

	@PrePersist
	private void initializeCreated() {
		if(null == created) {
			created = new Date();
		}
	}
}

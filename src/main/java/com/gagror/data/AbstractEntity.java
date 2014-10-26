package com.gagror.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

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

	// TODO Can setter for creation timestamp be something else than public?
	@Column(nullable = false, insertable = true, updatable = false)
	private Date created;

	// TODO Can this callback method be something else than public?
	@PrePersist
	public void initializeCreated() {
		if(null == created) {
			created = new Date();
		}
	}
}

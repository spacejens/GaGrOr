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
public abstract class AbstractEntity implements Identifiable<Long> {

	@Id
	@GeneratedValue
	@Getter
	private Long id;

	@Column(nullable = false, insertable = true, updatable = false)
	@Getter
	private Date created;

	@Override
	public final boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		if(null == other
				|| ! this.getClass().equals(other.getClass())
				|| null == getId()) {
			return false;
		}
		final AbstractEntity castOther = (AbstractEntity)other;
		return getId().equals(castOther.getId());
	}

	@Override
	public final int hashCode() {
		if(null == getId()) {
			return 0;
		} else {
			return getId().hashCode();
		}
	}

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

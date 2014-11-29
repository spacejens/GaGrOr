package com.gagror.data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@MappedSuperclass
public class AbstractEditableNamedEntity extends AbstractEditableEntity {

	@Column(nullable = false)
	@Getter
	@Setter
	private String name;

	public AbstractEditableNamedEntity(final String name) {
		setName(name);
	}

	@Override
	public String toString() {
		return String.format("%s, name='%s'", super.toString(), getName());
	}
}

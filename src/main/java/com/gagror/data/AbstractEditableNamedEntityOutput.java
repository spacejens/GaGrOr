package com.gagror.data;

import lombok.Getter;

public abstract class AbstractEditableNamedEntityOutput
extends AbstractEditableEntityOutput {

	@Getter
	private String name;

	protected AbstractEditableNamedEntityOutput(final AbstractEditableNamedEntity entity) {
		super(entity);
		name = entity.getName();
	}

	@Override
	public String toString() {
		return String.format("%s, name='%s'", super.toString(), getName());
	}
}

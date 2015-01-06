package com.gagror.data;

import lombok.Getter;

public abstract class AbstractEditableNamedEntityOutput
extends AbstractEditableEntityOutput
implements Comparable<AbstractEditableNamedEntityOutput> {

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

	@Override
	public int compareTo(final AbstractEditableNamedEntityOutput o) {
		return name.compareTo(o.getName());
	}
}

package com.gagror.data;

import java.util.Date;

import lombok.Getter;

public abstract class AbstractEditableEntityOutput
extends AbstractEntityOutput
implements Versioned {

	@Getter
	private final Long version;

	@Getter
	private final Date modified;

	protected AbstractEditableEntityOutput(final AbstractEditableEntity entity) {
		super(entity);
		version = entity.getVersion();
		modified = entity.getModified();
	}

	@Override
	public String toString() {
		return String.format("%s, version=%d", super.toString(), getVersion());
	}
}

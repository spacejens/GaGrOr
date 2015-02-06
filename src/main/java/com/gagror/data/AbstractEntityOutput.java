package com.gagror.data;

import java.util.Date;

import lombok.Getter;

public abstract class AbstractEntityOutput
extends AbstractIdentifiable
implements Output {

	@Getter
	private final Long id;

	@Getter
	private final Date created;

	protected AbstractEntityOutput(final AbstractEntity entity) {
		id = entity.getId();
		created = entity.getCreated();
	}
}

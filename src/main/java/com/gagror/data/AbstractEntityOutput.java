package com.gagror.data;

import java.util.Date;

import lombok.Getter;
import lombok.ToString;

@ToString(of="id")
public abstract class AbstractEntityOutput
extends AbstractIdentifiable {

	@Getter
	private final Long id;

	@Getter
	private final Date created;

	// TODO Create AbstractEditableEntityOutput and AbstractEditableNamedEntityOutput

	protected AbstractEntityOutput(final AbstractEntity entity) {
		id = entity.getId();
		created = entity.getCreated();
	}
}

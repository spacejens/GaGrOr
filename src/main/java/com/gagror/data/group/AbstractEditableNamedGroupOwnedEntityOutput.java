package com.gagror.data.group;

import lombok.Getter;

import com.gagror.data.AbstractEditableNamedEntity;
import com.gagror.data.AbstractEditableNamedEntityOutput;

public abstract class AbstractEditableNamedGroupOwnedEntityOutput
extends AbstractEditableNamedEntityOutput
implements GroupOwnedOutput {

	@Getter
	private final GroupReferenceOutput group;

	protected <E extends AbstractEditableNamedEntity & GroupOwned> AbstractEditableNamedGroupOwnedEntityOutput(final E entity) {
		super(entity);
		group = new GroupReferenceOutput(entity.getGroup());
	}
}

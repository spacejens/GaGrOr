package com.gagror.data.wh40kskirmish;

import lombok.Getter;

import com.gagror.data.group.GroupReferenceOutput;

public class Wh40kSkirmishGangTypeOutput extends Wh40kSkirmishGangTypeReferenceOutput {

	@Getter
	private final GroupReferenceOutput group;

	public Wh40kSkirmishGangTypeOutput(final Wh40kSkirmishGangTypeEntity entity, final GroupReferenceOutput group) {
		super(entity);
		this.group = group;
	}
}

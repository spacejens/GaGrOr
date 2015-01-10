package com.gagror.data.wh40kskirmish;

import lombok.Getter;

public class Wh40kSkirmishGangTypeOutput extends Wh40kSkirmishGangTypeReferenceOutput {

	@Getter
	private Long groupId;

	public Wh40kSkirmishGangTypeOutput(final Wh40kSkirmishGangTypeEntity entity) {
		super(entity);
		groupId = entity.getRules().getGroup().getId();
	}
}

package com.gagror.data.wh40kskirmish;

import lombok.Getter;

public class Wh40kSkirmishFactionOutput extends Wh40kSkirmishFactionReferenceOutput {

	@Getter
	private Long groupId;

	@Getter
	private Long gangTypeId;

	public Wh40kSkirmishFactionOutput(final Wh40kSkirmishFactionEntity entity) {
		super(entity);
		groupId = entity.getGangType().getRules().getGroup().getId();
		gangTypeId = entity.getGangType().getId();
	}
}

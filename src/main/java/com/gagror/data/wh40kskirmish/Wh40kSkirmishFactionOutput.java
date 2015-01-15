package com.gagror.data.wh40kskirmish;

import lombok.Getter;

public class Wh40kSkirmishFactionOutput extends Wh40kSkirmishFactionReferenceOutput {

	@Getter
	private Wh40kSkirmishGangTypeOutput gangType;

	public Wh40kSkirmishFactionOutput(
			final Wh40kSkirmishFactionEntity entity,
			final Wh40kSkirmishGangTypeOutput gangType) {
		super(entity);
		this.gangType = gangType;
	}
}

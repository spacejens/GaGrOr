package com.gagror.data.wh40kskirmish.rules.gangs;

import lombok.Getter;

public class Wh40kSkirmishRaceOutput extends Wh40kSkirmishRaceReferenceOutput {

	@Getter
	private final Wh40kSkirmishGangTypeOutput gangType;

	public Wh40kSkirmishRaceOutput(
			final Wh40kSkirmishRaceEntity entity,
			final Wh40kSkirmishGangTypeOutput gangType) {
		super(entity);
		this.gangType = gangType;
	}
}

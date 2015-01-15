package com.gagror.data.wh40kskirmish;

import lombok.Getter;

public class Wh40kSkirmishRaceOutput extends Wh40kSkirmishRaceReferenceOutput {

	@Getter
	private Wh40kSkirmishGangTypeOutput gangType;

	public Wh40kSkirmishRaceOutput(
			final Wh40kSkirmishRaceEntity entity,
			final Wh40kSkirmishGangTypeOutput gangType) {
		super(entity);
		this.gangType = gangType;
	}
}
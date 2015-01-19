package com.gagror.data.wh40kskirmish.rules.gangs;

import lombok.Getter;

public class Wh40kSkirmishFighterTypeOutput extends Wh40kSkirmishFighterTypeReferenceOutput {

	@Getter
	private final Wh40kSkirmishRaceOutput race;

	public Wh40kSkirmishFighterTypeOutput(
			final Wh40kSkirmishFighterTypeEntity entity,
			final Wh40kSkirmishRaceOutput race) {
		super(entity);
		this.race = race;
	}
}

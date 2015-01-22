package com.gagror.data.wh40kskirmish.rules.experience;

import lombok.Getter;

import com.gagror.data.AbstractEditableNamedEntityOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeOutput;

public class Wh40kSkirmishExperienceLevelOutput
extends AbstractEditableNamedEntityOutput
implements ExperiencePoints {

	@Getter
	private final Wh40kSkirmishGangTypeOutput gangType;

	@Getter
	private final int experiencePoints;

	public Wh40kSkirmishExperienceLevelOutput(
			final Wh40kSkirmishExperienceLevelEntity entity, final Wh40kSkirmishGangTypeOutput gangType) {
		super(entity);
		this.gangType = gangType;
		experiencePoints = entity.getExperiencePoints();
	}
}

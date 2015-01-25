package com.gagror.data.wh40kskirmish.rules.experience;

import lombok.Getter;

import com.gagror.data.AbstractEditableNamedEntityOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeOutput;

public class ExperienceLevelOutput
extends AbstractEditableNamedEntityOutput
implements ExperiencePoints {

	@Getter
	private final GangTypeOutput gangType;

	@Getter
	private final int experiencePoints;

	public ExperienceLevelOutput(
			final ExperienceLevelEntity entity, final GangTypeOutput gangType) {
		super(entity);
		this.gangType = gangType;
		experiencePoints = entity.getExperiencePoints();
	}
}

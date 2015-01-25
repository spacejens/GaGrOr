package com.gagror.data.wh40kskirmish.rules.gangs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.experience.ExperiencePointsComparator;
import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelEntity;
import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelOutput;

public class GangTypeOutput extends GangTypeReferenceOutput {

	@Getter
	private final GroupReferenceOutput group;

	@Getter
	private final List<ExperienceLevelOutput> experienceLevels;

	public GangTypeOutput(final GangTypeEntity entity, final GroupReferenceOutput group) {
		super(entity);
		this.group = group;
		// Sorted list of experience levels
		final List<ExperienceLevelOutput> tempExperienceLevels = new ArrayList<>();
		for(final ExperienceLevelEntity experienceLevel : entity.getExperienceLevels()) {
			tempExperienceLevels.add(new ExperienceLevelOutput(experienceLevel, this));
		}
		Collections.sort(tempExperienceLevels, ExperiencePointsComparator.getInstance());
		experienceLevels = Collections.unmodifiableList(tempExperienceLevels);
	}
}

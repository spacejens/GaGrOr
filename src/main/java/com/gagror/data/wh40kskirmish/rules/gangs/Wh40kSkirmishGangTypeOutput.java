package com.gagror.data.wh40kskirmish.rules.gangs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.experience.ExperiencePointsComparator;
import com.gagror.data.wh40kskirmish.rules.experience.Wh40kSkirmishExperienceLevelEntity;
import com.gagror.data.wh40kskirmish.rules.experience.Wh40kSkirmishExperienceLevelOutput;

public class Wh40kSkirmishGangTypeOutput extends Wh40kSkirmishGangTypeReferenceOutput {

	@Getter
	private final GroupReferenceOutput group;

	@Getter
	private final List<Wh40kSkirmishExperienceLevelOutput> experienceLevels;

	public Wh40kSkirmishGangTypeOutput(final Wh40kSkirmishGangTypeEntity entity, final GroupReferenceOutput group) {
		super(entity);
		this.group = group;
		// Sorted list of experience levels
		final List<Wh40kSkirmishExperienceLevelOutput> tempExperienceLevels = new ArrayList<>();
		for(final Wh40kSkirmishExperienceLevelEntity experienceLevel : entity.getExperienceLevels()) {
			tempExperienceLevels.add(new Wh40kSkirmishExperienceLevelOutput(experienceLevel, this));
		}
		Collections.sort(tempExperienceLevels, ExperiencePointsComparator.getInstance());
		experienceLevels = Collections.unmodifiableList(tempExperienceLevels);
	}
}

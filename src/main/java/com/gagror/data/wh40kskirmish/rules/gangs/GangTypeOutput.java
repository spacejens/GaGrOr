package com.gagror.data.wh40kskirmish.rules.gangs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelEntity;
import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelOutput;
import com.gagror.data.wh40kskirmish.rules.experience.ExperiencePointsComparator;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryReferenceOutput;

public class GangTypeOutput extends GangTypeReferenceOutput {

	@Getter
	private final List<ExperienceLevelOutput> experienceLevels;

	@Getter
	private final List<TerritoryCategoryReferenceOutput> startingTerritories;

	public GangTypeOutput(final GangTypeEntity entity) {
		super(entity);
		// Sorted list of experience levels
		final List<ExperienceLevelOutput> tempExperienceLevels = new ArrayList<>();
		for(final ExperienceLevelEntity experienceLevel : entity.getExperienceLevels()) {
			tempExperienceLevels.add(new ExperienceLevelOutput(experienceLevel, this));
		}
		Collections.sort(tempExperienceLevels, ExperiencePointsComparator.getInstance());
		experienceLevels = Collections.unmodifiableList(tempExperienceLevels);
		// Sorted list of starting territories
		final List<TerritoryCategoryReferenceOutput> tempStartingTerritories = new ArrayList<>();
		for(final StartingTerritoryEntity startingTerritory : entity.getStartingTerritories()) {
			tempStartingTerritories.add(new TerritoryCategoryReferenceOutput(startingTerritory.getTerritoryCategory()));
		}
		Collections.sort(tempStartingTerritories);
		startingTerritories = Collections.unmodifiableList(tempStartingTerritories);
	}
}

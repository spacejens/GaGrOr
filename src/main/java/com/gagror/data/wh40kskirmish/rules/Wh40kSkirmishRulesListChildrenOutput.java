package com.gagror.data.wh40kskirmish.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeListChildrenOutput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryListChildrenOutput;

public class Wh40kSkirmishRulesListChildrenOutput
extends Wh40kSkirmishRulesOutput {

	@Getter
	private final List<Wh40kSkirmishGangTypeListChildrenOutput> gangTypes;

	@Getter
	private final List<Wh40kSkirmishTerritoryCategoryListChildrenOutput> territoryCategories;

	public Wh40kSkirmishRulesListChildrenOutput(final Wh40kSkirmishRulesEntity entity, final GroupReferenceOutput group) {
		super(entity, group);
		// Sorted list of gang types and their factions, races, and fighter types
		final List<Wh40kSkirmishGangTypeListChildrenOutput> tempGangTypes = new ArrayList<>();
		for(final Wh40kSkirmishGangTypeEntity gangType : entity.getGangTypes()) {
			tempGangTypes.add(new Wh40kSkirmishGangTypeListChildrenOutput(gangType));
		}
		Collections.sort(tempGangTypes);
		gangTypes = Collections.unmodifiableList(tempGangTypes);
		// Sorted list of territory categories and their territory types
		final List<Wh40kSkirmishTerritoryCategoryListChildrenOutput> tempTerritoryCategories = new ArrayList<>();
		for(final Wh40kSkirmishTerritoryCategoryEntity territoryCategory : entity.getTerritoryCategories()) {
			tempTerritoryCategories.add(new Wh40kSkirmishTerritoryCategoryListChildrenOutput(territoryCategory));
		}
		Collections.sort(tempTerritoryCategories);
		territoryCategories = Collections.unmodifiableList(tempTerritoryCategories);
	}
}

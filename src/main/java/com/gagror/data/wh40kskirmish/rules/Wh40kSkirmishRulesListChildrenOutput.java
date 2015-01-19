package com.gagror.data.wh40kskirmish.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeListChildrenOutput;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryListChildrenOutput;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryListChildrenOutput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryListChildrenOutput;

public class Wh40kSkirmishRulesListChildrenOutput
extends Wh40kSkirmishRulesOutput {

	@Getter
	private final List<Wh40kSkirmishGangTypeListChildrenOutput> gangTypes;

	@Getter
	private final List<Wh40kSkirmishTerritoryCategoryListChildrenOutput> territoryCategories;

	@Getter
	private final List<Wh40kSkirmishSkillCategoryListChildrenOutput> skillCategories;

	@Getter
	private final List<Wh40kSkirmishItemCategoryListChildrenOutput> itemCategories;

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
		// Sorted list of skill categories and their skills
		final List<Wh40kSkirmishSkillCategoryListChildrenOutput> tempSkillCategories = new ArrayList<>();
		for(final Wh40kSkirmishSkillCategoryEntity skillCategory : entity.getSkillCategories()) {
			tempSkillCategories.add(new Wh40kSkirmishSkillCategoryListChildrenOutput(skillCategory));
		}
		Collections.sort(tempSkillCategories);
		skillCategories = Collections.unmodifiableList(tempSkillCategories);
		// Sorted list of item categories and their item types
		final List<Wh40kSkirmishItemCategoryListChildrenOutput> tempItemCategories = new ArrayList<>();
		for(final Wh40kSkirmishItemCategoryEntity itemCategory : entity.getItemCategories()) {
			tempItemCategories.add(new Wh40kSkirmishItemCategoryListChildrenOutput(itemCategory));
		}
		Collections.sort(tempItemCategories);
		itemCategories = Collections.unmodifiableList(tempItemCategories);
	}
}

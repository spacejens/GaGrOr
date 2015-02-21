package com.gagror.data.wh40kskirmish.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeListChildrenOutput;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryListChildrenOutput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryListChildrenOutput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryListChildrenOutput;

public class RulesListChildrenOutput
extends RulesOutput {

	@Getter
	private final List<GangTypeListChildrenOutput> gangTypes;

	@Getter
	private final List<TerritoryCategoryListChildrenOutput> territoryCategories;

	@Getter
	private final List<SkillCategoryListChildrenOutput> skillCategories;

	@Getter
	private final List<ItemCategoryListChildrenOutput> itemCategories;

	public RulesListChildrenOutput(final Wh40kSkirmishRulesEntity entity) {
		super(entity);
		// Sorted list of gang types and their factions, races, and fighter types
		final List<GangTypeListChildrenOutput> tempGangTypes = new ArrayList<>();
		for(final GangTypeEntity gangType : entity.getGangTypes()) {
			tempGangTypes.add(new GangTypeListChildrenOutput(gangType));
		}
		Collections.sort(tempGangTypes);
		gangTypes = Collections.unmodifiableList(tempGangTypes);
		// Sorted list of territory categories and their territory types
		final List<TerritoryCategoryListChildrenOutput> tempTerritoryCategories = new ArrayList<>();
		for(final TerritoryCategoryEntity territoryCategory : entity.getTerritoryCategories()) {
			tempTerritoryCategories.add(new TerritoryCategoryListChildrenOutput(territoryCategory));
		}
		Collections.sort(tempTerritoryCategories);
		territoryCategories = Collections.unmodifiableList(tempTerritoryCategories);
		// Sorted list of skill categories and their skills
		final List<SkillCategoryListChildrenOutput> tempSkillCategories = new ArrayList<>();
		for(final SkillCategoryEntity skillCategory : entity.getSkillCategories()) {
			tempSkillCategories.add(new SkillCategoryListChildrenOutput(skillCategory));
		}
		Collections.sort(tempSkillCategories);
		skillCategories = Collections.unmodifiableList(tempSkillCategories);
		// Sorted list of item categories and their item types
		final List<ItemCategoryListChildrenOutput> tempItemCategories = new ArrayList<>();
		for(final ItemCategoryEntity itemCategory : entity.getItemCategories()) {
			tempItemCategories.add(new ItemCategoryListChildrenOutput(itemCategory));
		}
		Collections.sort(tempItemCategories);
		itemCategories = Collections.unmodifiableList(tempItemCategories);
	}
}

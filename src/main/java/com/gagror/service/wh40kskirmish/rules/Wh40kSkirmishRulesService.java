package com.gagror.service.wh40kskirmish.rules;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesListChildrenOutput;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFactionOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceOutput;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemTypeEntity;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemTypeOutput;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillEntity;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillOutput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryTypeEntity;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryTypeOutput;
import com.gagror.service.social.GroupService;

@Service
@Transactional
@CommonsLog
public class Wh40kSkirmishRulesService {

	@Autowired
	GroupService groupService;

	public Wh40kSkirmishRulesListChildrenOutput viewRulesListChildren(final Long groupId) {
		log.debug(String.format("Viewing rules (including children data) for group %d", groupId));
		return new Wh40kSkirmishRulesListChildrenOutput(loadRules(groupId), groupService.viewGroup(groupId));
	}

	public Wh40kSkirmishRulesOutput viewRules(final Long groupId) {
		log.debug(String.format("Viewing rules for group %d", groupId));
		return new Wh40kSkirmishRulesOutput(loadRules(groupId), groupService.viewGroup(groupId));
	}

	private Wh40kSkirmishRulesEntity loadRules(final Long groupId) {
		final GroupEntity group = groupService.loadGroup(groupId);
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		return group.getWh40kSkirmishRules();
	}

	private Wh40kSkirmishGangTypeEntity loadGangType(final Long groupId, final Long gangTypeId) {
		final Wh40kSkirmishRulesEntity rules = loadRules(groupId);
		for(final Wh40kSkirmishGangTypeEntity gangType : rules.getGangTypes()) {
			if(gangType.getId().equals(gangTypeId)) {
				return gangType;
			}
		}
		throw new DataNotFoundException(String.format("Gang type %d (group %d)", gangTypeId, groupId));
	}

	public Wh40kSkirmishGangTypeOutput viewGangType(final Long groupId, final Long gangTypeId) {
		return new Wh40kSkirmishGangTypeOutput(loadGangType(groupId, gangTypeId), groupService.viewGroup(groupId));
	}

	public Wh40kSkirmishFactionEntity loadFaction(final Long groupId, final Long gangTypeId, final Long factionId) {
		final Wh40kSkirmishGangTypeEntity gangType = loadGangType(groupId, gangTypeId);
		for(final Wh40kSkirmishFactionEntity faction : gangType.getFactions()) {
			if(faction.getId().equals(factionId)) {
				return faction;
			}
		}
		throw new DataNotFoundException(String.format("Faction %d (gang type %d, group %d)",
				factionId, gangTypeId, groupId));
	}

	public Wh40kSkirmishFactionOutput viewFaction(final Long groupId, final Long gangTypeId, final Long factionId) {
		return new Wh40kSkirmishFactionOutput(
				loadFaction(groupId, gangTypeId, factionId),
				viewGangType(groupId, gangTypeId));
	}

	private Wh40kSkirmishRaceEntity loadRace(final Long groupId, final Long gangTypeId, final Long raceId) {
		final Wh40kSkirmishGangTypeEntity gangType = loadGangType(groupId, gangTypeId);
		for(final Wh40kSkirmishRaceEntity race : gangType.getRaces()) {
			if(race.getId().equals(raceId)) {
				return race;
			}
		}
		throw new DataNotFoundException(String.format("Race %d (gang type %d, group %d)",
				raceId, gangTypeId, groupId));
	}

	public Wh40kSkirmishRaceOutput viewRace(final Long groupId, final Long gangTypeId, final Long raceId) {
		return new Wh40kSkirmishRaceOutput(
				loadRace(groupId, gangTypeId, raceId),
				viewGangType(groupId, gangTypeId));
	}

	private Wh40kSkirmishFighterTypeEntity loadFighterType(
			final Long groupId,
			final Long gangTypeId,
			final Long raceId,
			final Long fighterTypeId) {
		final Wh40kSkirmishRaceEntity race = loadRace(groupId, gangTypeId, raceId);
		for(final Wh40kSkirmishFighterTypeEntity fighterType : race.getFighterTypes()) {
			if(fighterType.getId().equals(fighterTypeId)){
				return fighterType;
			}
		}
		throw new DataNotFoundException(String.format(
				"Fighter type %d (race %d, gang type %d, group %d)",
				fighterTypeId, raceId, gangTypeId, groupId));
	}

	public Wh40kSkirmishFighterTypeOutput viewFighterType(
			final Long groupId,
			final Long gangTypeId,
			final Long raceId,
			final Long fighterTypeId) {
		return new Wh40kSkirmishFighterTypeOutput(
				loadFighterType(groupId, gangTypeId, raceId, fighterTypeId),
				viewRace(groupId, gangTypeId, raceId));
	}

	private Wh40kSkirmishTerritoryCategoryEntity loadTerritoryCategory(final Long groupId, final Long territoryCategoryId) {
		final Wh40kSkirmishRulesEntity rules = loadRules(groupId);
		for(final Wh40kSkirmishTerritoryCategoryEntity territoryCategory : rules.getTerritoryCategories()) {
			if(territoryCategory.getId().equals(territoryCategoryId)) {
				return territoryCategory;
			}
		}
		throw new DataNotFoundException(String.format("Territory category %d (group %d)", territoryCategoryId, groupId));
	}

	public Wh40kSkirmishTerritoryCategoryOutput viewTerritoryCategory(final Long groupId, final Long territoryCategoryId) {
		return new Wh40kSkirmishTerritoryCategoryOutput(loadTerritoryCategory(groupId, territoryCategoryId), groupService.viewGroup(groupId));
	}

	private Wh40kSkirmishTerritoryTypeEntity loadTerritoryType(final Long groupId, final Long territoryCategoryId, final Long territoryTypeId) {
		final Wh40kSkirmishTerritoryCategoryEntity territoryCategory = loadTerritoryCategory(groupId, territoryCategoryId);
		for(final Wh40kSkirmishTerritoryTypeEntity territoryType : territoryCategory.getTerritoryTypes()) {
			if(territoryType.getId().equals(territoryTypeId)) {
				return territoryType;
			}
		}
		throw new DataNotFoundException(String.format("Territory type %d (territory category %d, group %d)",
				territoryTypeId, territoryCategoryId, groupId));
	}

	public Wh40kSkirmishTerritoryTypeOutput viewTerritoryType(final Long groupId, final Long territoryCategoryId, final Long territoryTypeId) {
		return new Wh40kSkirmishTerritoryTypeOutput(
				loadTerritoryType(groupId, territoryCategoryId, territoryTypeId),
				viewTerritoryCategory(groupId, territoryCategoryId));
	}

	private Wh40kSkirmishSkillCategoryEntity loadSkillCategory(final Long groupId, final Long skillCategoryId) {
		final Wh40kSkirmishRulesEntity rules = loadRules(groupId);
		for(final Wh40kSkirmishSkillCategoryEntity skillCategory : rules.getSkillCategories()) {
			if(skillCategory.getId().equals(skillCategoryId)) {
				return skillCategory;
			}
		}
		throw new DataNotFoundException(String.format("Skill category %d (group %d)", skillCategoryId, groupId));
	}

	public Wh40kSkirmishSkillCategoryOutput viewSkillCategory(final Long groupId, final Long skillCategoryId) {
		return new Wh40kSkirmishSkillCategoryOutput(loadSkillCategory(groupId, skillCategoryId), groupService.viewGroup(groupId));
	}

	private Wh40kSkirmishSkillEntity loadSkill(final Long groupId, final Long skillCategoryId, final Long skillId) {
		final Wh40kSkirmishSkillCategoryEntity skillCategory = loadSkillCategory(groupId, skillCategoryId);
		for(final Wh40kSkirmishSkillEntity skill : skillCategory.getSkills()) {
			if(skill.getId().equals(skillId)) {
				return skill;
			}
		}
		throw new DataNotFoundException(String.format("Skill %d (skill category %d, group %d)",
				skillId, skillCategoryId, groupId));
	}

	public Wh40kSkirmishSkillOutput viewSkill(final Long groupId, final Long skillCategoryId, final Long skillId) {
		return new Wh40kSkirmishSkillOutput(
				loadSkill(groupId, skillCategoryId, skillId),
				viewSkillCategory(groupId, skillCategoryId));
	}

	private Wh40kSkirmishItemCategoryEntity loadItemCategory(final Long groupId, final Long itemCategoryId) {
		final Wh40kSkirmishRulesEntity rules = loadRules(groupId);
		for(final Wh40kSkirmishItemCategoryEntity itemCategory : rules.getItemCategories()) {
			if(itemCategory.getId().equals(itemCategoryId)) {
				return itemCategory;
			}
		}
		throw new DataNotFoundException(String.format("Item category %d (group %d)", itemCategoryId, groupId));
	}

	public Wh40kSkirmishItemCategoryOutput viewItemCategory(final Long groupId, final Long itemCategoryId) {
		return new Wh40kSkirmishItemCategoryOutput(loadItemCategory(groupId, itemCategoryId), groupService.viewGroup(groupId));
	}

	private Wh40kSkirmishItemTypeEntity loadItemType(final Long groupId, final Long itemCategoryId, final Long itemTypeId) {
		final Wh40kSkirmishItemCategoryEntity itemCategory = loadItemCategory(groupId, itemCategoryId);
		for(final Wh40kSkirmishItemTypeEntity itemType : itemCategory.getItemTypes()) {
			if(itemType.getId().equals(itemTypeId)) {
				return itemType;
			}
		}
		throw new DataNotFoundException(String.format("Item type %d (item category %d, group %d)",
				itemTypeId, itemCategoryId, groupId));
	}

	public Wh40kSkirmishItemTypeOutput viewItemType(final Long groupId, final Long itemCategoryId, final Long itemTypeId) {
		return new Wh40kSkirmishItemTypeOutput(
				loadItemType(groupId, itemCategoryId, itemTypeId),
				viewItemCategory(groupId, itemCategoryId));
	}
}

package com.gagror.service.wh40kskirmish.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.RulesListChildrenOutput;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceOutput;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeOutput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillOutput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeOutput;
import com.gagror.service.social.GroupService;

@Service
@Transactional
@CommonsLog
public class RulesService {

	@Autowired
	GroupService groupService;

	public RulesListChildrenOutput viewRulesListChildren(final Long groupId) {
		log.debug(String.format("Viewing rules (including children data) for group %d", groupId));
		return new RulesListChildrenOutput(loadRules(groupId), groupService.viewGroup(groupId));
	}

	public RulesOutput viewRules(final Long groupId) {
		log.debug(String.format("Viewing rules for group %d", groupId));
		return new RulesOutput(loadRules(groupId), groupService.viewGroup(groupId));
	}

	private Wh40kSkirmishRulesEntity loadRules(final Long groupId) {
		final GroupEntity group = groupService.loadGroup(groupId);
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		return group.getWh40kSkirmishRules();
	}

	public List<FactionReferenceOutput> listAllFactions(final Long groupId) {
		final List<FactionReferenceOutput> factions = new ArrayList<>();
		final Wh40kSkirmishRulesEntity rules = loadRules(groupId);
		for(final GangTypeEntity gangType : rules.getGangTypes()) {
			for(final FactionEntity faction : gangType.getFactions()) {
				factions.add(new FactionReferenceOutput(faction));
			}
		}
		Collections.sort(factions);
		return factions;
	}

	private GangTypeEntity loadGangType(final Long groupId, final Long gangTypeId) {
		final Wh40kSkirmishRulesEntity rules = loadRules(groupId);
		for(final GangTypeEntity gangType : rules.getGangTypes()) {
			if(gangType.getId().equals(gangTypeId)) {
				return gangType;
			}
		}
		throw new DataNotFoundException(String.format("Gang type %d (group %d)", gangTypeId, groupId));
	}

	public GangTypeOutput viewGangType(final Long groupId, final Long gangTypeId) {
		return new GangTypeOutput(loadGangType(groupId, gangTypeId), groupService.viewGroup(groupId));
	}

	public FactionEntity loadFaction(final Long groupId, final Long gangTypeId, final Long factionId) {
		final GangTypeEntity gangType = loadGangType(groupId, gangTypeId);
		for(final FactionEntity faction : gangType.getFactions()) {
			if(faction.getId().equals(factionId)) {
				return faction;
			}
		}
		throw new DataNotFoundException(String.format("Faction %d (gang type %d, group %d)",
				factionId, gangTypeId, groupId));
	}

	public FactionOutput viewFaction(final Long groupId, final Long gangTypeId, final Long factionId) {
		return new FactionOutput(
				loadFaction(groupId, gangTypeId, factionId),
				viewGangType(groupId, gangTypeId));
	}

	private RaceEntity loadRace(final Long groupId, final Long gangTypeId, final Long raceId) {
		final GangTypeEntity gangType = loadGangType(groupId, gangTypeId);
		for(final RaceEntity race : gangType.getRaces()) {
			if(race.getId().equals(raceId)) {
				return race;
			}
		}
		throw new DataNotFoundException(String.format("Race %d (gang type %d, group %d)",
				raceId, gangTypeId, groupId));
	}

	public RaceOutput viewRace(final Long groupId, final Long gangTypeId, final Long raceId) {
		return new RaceOutput(
				loadRace(groupId, gangTypeId, raceId),
				viewGangType(groupId, gangTypeId));
	}

	private FighterTypeEntity loadFighterType(
			final Long groupId,
			final Long gangTypeId,
			final Long raceId,
			final Long fighterTypeId) {
		final RaceEntity race = loadRace(groupId, gangTypeId, raceId);
		for(final FighterTypeEntity fighterType : race.getFighterTypes()) {
			if(fighterType.getId().equals(fighterTypeId)){
				return fighterType;
			}
		}
		throw new DataNotFoundException(String.format(
				"Fighter type %d (race %d, gang type %d, group %d)",
				fighterTypeId, raceId, gangTypeId, groupId));
	}

	public FighterTypeOutput viewFighterType(
			final Long groupId,
			final Long gangTypeId,
			final Long raceId,
			final Long fighterTypeId) {
		return new FighterTypeOutput(
				loadFighterType(groupId, gangTypeId, raceId, fighterTypeId),
				viewRace(groupId, gangTypeId, raceId));
	}

	private TerritoryCategoryEntity loadTerritoryCategory(final Long groupId, final Long territoryCategoryId) {
		final Wh40kSkirmishRulesEntity rules = loadRules(groupId);
		for(final TerritoryCategoryEntity territoryCategory : rules.getTerritoryCategories()) {
			if(territoryCategory.getId().equals(territoryCategoryId)) {
				return territoryCategory;
			}
		}
		throw new DataNotFoundException(String.format("Territory category %d (group %d)", territoryCategoryId, groupId));
	}

	public TerritoryCategoryOutput viewTerritoryCategory(final Long groupId, final Long territoryCategoryId) {
		return new TerritoryCategoryOutput(loadTerritoryCategory(groupId, territoryCategoryId), groupService.viewGroup(groupId));
	}

	private TerritoryTypeEntity loadTerritoryType(final Long groupId, final Long territoryCategoryId, final Long territoryTypeId) {
		final TerritoryCategoryEntity territoryCategory = loadTerritoryCategory(groupId, territoryCategoryId);
		for(final TerritoryTypeEntity territoryType : territoryCategory.getTerritoryTypes()) {
			if(territoryType.getId().equals(territoryTypeId)) {
				return territoryType;
			}
		}
		throw new DataNotFoundException(String.format("Territory type %d (territory category %d, group %d)",
				territoryTypeId, territoryCategoryId, groupId));
	}

	public TerritoryTypeOutput viewTerritoryType(final Long groupId, final Long territoryCategoryId, final Long territoryTypeId) {
		return new TerritoryTypeOutput(
				loadTerritoryType(groupId, territoryCategoryId, territoryTypeId),
				viewTerritoryCategory(groupId, territoryCategoryId));
	}

	private SkillCategoryEntity loadSkillCategory(final Long groupId, final Long skillCategoryId) {
		final Wh40kSkirmishRulesEntity rules = loadRules(groupId);
		for(final SkillCategoryEntity skillCategory : rules.getSkillCategories()) {
			if(skillCategory.getId().equals(skillCategoryId)) {
				return skillCategory;
			}
		}
		throw new DataNotFoundException(String.format("Skill category %d (group %d)", skillCategoryId, groupId));
	}

	public SkillCategoryOutput viewSkillCategory(final Long groupId, final Long skillCategoryId) {
		return new SkillCategoryOutput(loadSkillCategory(groupId, skillCategoryId), groupService.viewGroup(groupId));
	}

	private SkillEntity loadSkill(final Long groupId, final Long skillCategoryId, final Long skillId) {
		final SkillCategoryEntity skillCategory = loadSkillCategory(groupId, skillCategoryId);
		for(final SkillEntity skill : skillCategory.getSkills()) {
			if(skill.getId().equals(skillId)) {
				return skill;
			}
		}
		throw new DataNotFoundException(String.format("Skill %d (skill category %d, group %d)",
				skillId, skillCategoryId, groupId));
	}

	public SkillOutput viewSkill(final Long groupId, final Long skillCategoryId, final Long skillId) {
		return new SkillOutput(
				loadSkill(groupId, skillCategoryId, skillId),
				viewSkillCategory(groupId, skillCategoryId));
	}

	private ItemCategoryEntity loadItemCategory(final Long groupId, final Long itemCategoryId) {
		final Wh40kSkirmishRulesEntity rules = loadRules(groupId);
		for(final ItemCategoryEntity itemCategory : rules.getItemCategories()) {
			if(itemCategory.getId().equals(itemCategoryId)) {
				return itemCategory;
			}
		}
		throw new DataNotFoundException(String.format("Item category %d (group %d)", itemCategoryId, groupId));
	}

	public ItemCategoryOutput viewItemCategory(final Long groupId, final Long itemCategoryId) {
		return new ItemCategoryOutput(loadItemCategory(groupId, itemCategoryId), groupService.viewGroup(groupId));
	}

	private ItemTypeEntity loadItemType(final Long groupId, final Long itemCategoryId, final Long itemTypeId) {
		final ItemCategoryEntity itemCategory = loadItemCategory(groupId, itemCategoryId);
		for(final ItemTypeEntity itemType : itemCategory.getItemTypes()) {
			if(itemType.getId().equals(itemTypeId)) {
				return itemType;
			}
		}
		throw new DataNotFoundException(String.format("Item type %d (item category %d, group %d)",
				itemTypeId, itemCategoryId, groupId));
	}

	public ItemTypeOutput viewItemType(final Long groupId, final Long itemCategoryId, final Long itemTypeId) {
		return new ItemTypeOutput(
				loadItemType(groupId, itemCategoryId, itemTypeId),
				viewItemCategory(groupId, itemCategoryId));
	}
}

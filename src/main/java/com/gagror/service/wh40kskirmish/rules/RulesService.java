package com.gagror.service.wh40kskirmish.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.RulesListChildrenOutput;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;
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
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryRepository;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeOutput;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeRepository;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryRepository;
import com.gagror.data.wh40kskirmish.rules.skills.SkillOutput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillRepository;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryRepository;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeOutput;
import com.gagror.service.social.GroupService;

@Service
@Transactional
@CommonsLog
public class RulesService {

	@Autowired
	GroupService groupService;

	@Autowired
	RulesRepository rulesRepository;

	@Autowired
	SkillCategoryRepository skillCategoryRepository;

	@Autowired
	SkillRepository skillRepository;

	@Autowired
	ItemCategoryRepository itemCategoryRepository;

	@Autowired
	ItemTypeRepository itemTypeRepository;

	@Autowired
	TerritoryCategoryRepository territoryCategoryRepository;

	public RulesListChildrenOutput viewRulesListChildren(final Long groupId) {
		log.debug(String.format("Viewing rules (including children data) for group %d", groupId));
		return new RulesListChildrenOutput(rulesRepository.load(groupId), groupService.viewGroup(groupId));
	}

	public RulesOutput viewRules(final Long groupId) {
		log.debug(String.format("Viewing rules for group %d", groupId));
		return new RulesOutput(rulesRepository.load(groupId), groupService.viewGroup(groupId));
	}

	public List<FactionReferenceOutput> listAllFactions(final Long groupId) {
		final List<FactionReferenceOutput> factions = new ArrayList<>();
		final Wh40kSkirmishRulesEntity rules = rulesRepository.load(groupId);
		for(final GangTypeEntity gangType : rules.getGangTypes()) {
			for(final FactionEntity faction : gangType.getFactions()) {
				factions.add(new FactionReferenceOutput(faction));
			}
		}
		Collections.sort(factions);
		return factions;
	}

	private GangTypeEntity loadGangType(final Long groupId, final Long gangTypeId) {
		final Wh40kSkirmishRulesEntity rules = rulesRepository.load(groupId);
		for(final GangTypeEntity gangType : rules.getGangTypes()) {
			if(gangType.hasId(gangTypeId)) {
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
			if(faction.hasId(factionId)) {
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
			if(race.hasId(raceId)) {
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
			if(fighterType.hasId(fighterTypeId)){
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

	public TerritoryCategoryOutput viewTerritoryCategory(final Long groupId, final Long territoryCategoryId) {
		return new TerritoryCategoryOutput(territoryCategoryRepository.load(groupId, territoryCategoryId), groupService.viewGroup(groupId));
	}

	private TerritoryTypeEntity loadTerritoryType(final Long groupId, final Long territoryCategoryId, final Long territoryTypeId) {
		final TerritoryCategoryEntity territoryCategory = territoryCategoryRepository.load(groupId, territoryCategoryId);
		for(final TerritoryTypeEntity territoryType : territoryCategory.getTerritoryTypes()) {
			if(territoryType.hasId(territoryTypeId)) {
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

	public SkillCategoryOutput viewSkillCategory(final Long groupId, final Long skillCategoryId) {
		return new SkillCategoryOutput(skillCategoryRepository.load(groupId, skillCategoryId), groupService.viewGroup(groupId));
	}

	public SkillOutput viewSkill(final Long groupId, final Long skillCategoryId, final Long skillId) {
		return new SkillOutput(
				skillRepository.load(groupId, skillCategoryId, skillId),
				viewSkillCategory(groupId, skillCategoryId));
	}

	public ItemCategoryOutput viewItemCategory(final Long groupId, final Long itemCategoryId) {
		return new ItemCategoryOutput(itemCategoryRepository.load(groupId, itemCategoryId), groupService.viewGroup(groupId));
	}

	public ItemTypeOutput viewItemType(final Long groupId, final Long itemCategoryId, final Long itemTypeId) {
		return new ItemTypeOutput(
				itemTypeRepository.load(groupId, itemCategoryId, itemTypeId),
				viewItemCategory(groupId, itemCategoryId));
	}
}

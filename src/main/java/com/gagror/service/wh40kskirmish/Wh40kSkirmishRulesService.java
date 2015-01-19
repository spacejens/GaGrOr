package com.gagror.service.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesListChildrenOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFactionOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceOutput;
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

	public Wh40kSkirmishRulesListChildrenOutput viewRules(final Long groupId) {
		log.debug(String.format("Viewing rules for group %d", groupId));
		return new Wh40kSkirmishRulesListChildrenOutput(loadRules(groupId), groupService.viewGroup(groupId));
	}

	private Wh40kSkirmishRulesEntity loadRules(final Long groupId) {
		final GroupEntity group = groupService.loadGroup(groupId);
		if(null == group.getWh40kSkirmishRules()) {
			throw new IllegalArgumentException(String.format("Group %s does not have WH40K skirmish rules", group));
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
		throw new IllegalArgumentException(String.format("Failed to find gang type %d in group %d", gangTypeId, groupId));
	}

	public Wh40kSkirmishGangTypeOutput viewGangType(final Long groupId, final Long gangTypeId) {
		return new Wh40kSkirmishGangTypeOutput(loadGangType(groupId, gangTypeId), groupService.viewGroup(groupId));
	}

	private Wh40kSkirmishFactionEntity loadFaction(final Long groupId, final Long gangTypeId, final Long factionId) {
		final Wh40kSkirmishGangTypeEntity gangType = loadGangType(groupId, gangTypeId);
		for(final Wh40kSkirmishFactionEntity faction : gangType.getFactions()) {
			if(faction.getId().equals(factionId)) {
				return faction;
			}
		}
		throw new IllegalArgumentException(String.format("Failed to find faction %d in gang type %d of group %d",
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
		throw new IllegalArgumentException(String.format("Failed to find race %d in gang type %d of group %d",
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
		throw new IllegalArgumentException(String.format(
				"Failed to find fighter type %d of race %d in gang type %d of group %d",
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
		throw new IllegalArgumentException(String.format("Failed to find territory category %d in group %d", territoryCategoryId, groupId));
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
		throw new IllegalArgumentException(String.format("Failed to find territory type %d in territory category %d of group %d",
				territoryTypeId, territoryCategoryId, groupId));
	}

	public Wh40kSkirmishTerritoryTypeOutput viewTerritoryType(final Long groupId, final Long territoryCategoryId, final Long territoryTypeId) {
		return new Wh40kSkirmishTerritoryTypeOutput(
				loadTerritoryType(groupId, territoryCategoryId, territoryTypeId),
				viewTerritoryCategory(groupId, territoryCategoryId));
	}
}

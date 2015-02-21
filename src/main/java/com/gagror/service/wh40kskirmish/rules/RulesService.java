package com.gagror.service.wh40kskirmish.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.wh40kskirmish.rules.RulesListChildrenOutput;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceRepository;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryRepository;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeOutput;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeRepository;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryRepository;
import com.gagror.data.wh40kskirmish.rules.skills.SkillEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillOutput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillRepository;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryRepository;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeOutput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeRepository;
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

	@Autowired
	TerritoryTypeRepository territoryTypeRepository;

	@Autowired
	GangTypeRepository gangTypeRepository;

	@Autowired
	FactionRepository factionRepository;

	@Autowired
	RaceRepository raceRepository;

	@Autowired
	FighterTypeRepository fighterTypeRepository;

	public RulesListChildrenOutput viewRulesListChildren(final Long groupId) {
		log.debug(String.format("Viewing rules (including children data) for group %d", groupId));
		return new RulesListChildrenOutput(rulesRepository.load(groupId));
	}

	public RulesOutput viewRules(final Long groupId) {
		log.debug(String.format("Viewing rules for group %d", groupId));
		return new RulesOutput(rulesRepository.load(groupId));
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

	public GangTypeOutput viewGangType(final Long groupId, final Long gangTypeId) {
		return new GangTypeOutput(gangTypeRepository.load(groupId, gangTypeId));
	}

	public FactionOutput viewFaction(final Long groupId, final Long factionId) {
		final FactionEntity faction = factionRepository.load(groupId, factionId);
		return new FactionOutput(
				faction,
				new GangTypeOutput(faction.getGangType()));
	}

	public RaceOutput viewRace(final Long groupId, final Long raceId) {
		final RaceEntity race = raceRepository.load(groupId, raceId);
		return new RaceOutput(
				race,
				new GangTypeOutput(race.getGangType()));
	}

	public FighterTypeOutput viewFighterType(final Long groupId, final Long fighterTypeId) {
		final FighterTypeEntity fighterType = fighterTypeRepository.load(groupId, fighterTypeId);
		return new FighterTypeOutput(
				fighterType,
				new RaceOutput(
						fighterType.getRace(),
						new GangTypeOutput(fighterType.getRace().getGangType())));
	}

	public TerritoryCategoryOutput viewTerritoryCategory(final Long groupId, final Long territoryCategoryId) {
		return new TerritoryCategoryOutput(territoryCategoryRepository.load(groupId, territoryCategoryId));
	}

	public TerritoryTypeOutput viewTerritoryType(final Long groupId, final Long territoryTypeId) {
		final TerritoryTypeEntity territoryType = territoryTypeRepository.load(groupId, territoryTypeId);
		return new TerritoryTypeOutput(
				territoryType,
				new TerritoryCategoryOutput(territoryType.getTerritoryCategory()));
	}

	public SkillCategoryOutput viewSkillCategory(final Long groupId, final Long skillCategoryId) {
		return new SkillCategoryOutput(skillCategoryRepository.load(groupId, skillCategoryId));
	}

	public SkillOutput viewSkill(final Long groupId, final Long skillId) {
		final SkillEntity skill = skillRepository.load(groupId, skillId);
		return new SkillOutput(
				skill,
				new SkillCategoryOutput(skill.getSkillCategory()));
	}

	public ItemCategoryOutput viewItemCategory(final Long groupId, final Long itemCategoryId) {
		return new ItemCategoryOutput(itemCategoryRepository.load(groupId, itemCategoryId));
	}

	public ItemTypeOutput viewItemType(final Long groupId, final Long itemTypeId) {
		final ItemTypeEntity itemType = itemTypeRepository.load(groupId, itemTypeId);
		return new ItemTypeOutput(
				itemType,
				new ItemCategoryOutput(itemType.getItemCategory()));
	}
}

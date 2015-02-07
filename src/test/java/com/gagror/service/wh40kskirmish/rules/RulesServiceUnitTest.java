package com.gagror.service.wh40kskirmish.rules;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.group.GroupEntity;
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
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryRepository;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeOutput;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeRepository;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryRepository;
import com.gagror.data.wh40kskirmish.rules.skills.SkillEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillOutput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillRepository;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryRepository;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeOutput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeRepository;
import com.gagror.service.social.GroupService;

@RunWith(MockitoJUnitRunner.class)
public class RulesServiceUnitTest {

	private static final Long GROUP_ID = 123L;
	private static final Long RULES_ID = 1L;
	private static final Long WRONG_TYPE_GROUP_ID = 456L;
	private static final Long GANG_TYPE_ID = 7L;
	private static final String GANG_TYPE_NAME = "Gang type";
	private static final Long FACTION_ID = 9L;
	private static final String FACTION_NAME = "Faction";
	private static final Long RACE_ID = 113L;
	private static final String RACE_NAME = "Race";
	private static final Long FIGHTER_TYPE_ID = 457L;
	private static final String FIGHTER_TYPE_NAME = "Fighter type";
	private static final Long TERRITORY_CATEGORY_ID = 67L;
	private static final String TERRITORY_CATEGORY_NAME = "Territory category";
	private static final Long TERRITORY_TYPE_ID = 983L;
	private static final String TERRITORY_TYPE_NAME = "Territory type";
	private static final Long SKILL_CATEGORY_ID = 74154L;
	private static final String SKILL_CATEGORY_NAME = "Skill category";
	private static final Long SKILL_ID = 9998L;
	private static final String SKILL_NAME = "Skill";
	private static final Long ITEM_CATEGORY_ID = 8986L;
	private static final String ITEM_CATEGORY_NAME = "Item category";
	private static final Long ITEM_TYPE_ID = 165L;
	private static final String ITEM_TYPE_NAME = "Item type";

	RulesService instance;

	@Mock
	GroupService groupService;

	@Mock
	RulesRepository rulesRepository;

	@Mock
	SkillCategoryRepository skillCategoryRepository;

	@Mock
	SkillRepository skillRepository;

	@Mock
	ItemCategoryRepository itemCategoryRepository;

	@Mock
	ItemTypeRepository itemTypeRepository;

	@Mock
	TerritoryCategoryRepository territoryCategoryRepository;

	@Mock
	TerritoryTypeRepository territoryTypeRepository;

	@Mock
	GangTypeRepository gangTypeRepository;

	@Mock
	FactionRepository factionRepository;

	@Mock
	RaceRepository raceRepository;

	@Mock
	FighterTypeRepository fighterTypeRepository;

	@Mock
	GroupEntity groupEntity;

	@Mock
	Wh40kSkirmishRulesEntity rulesEntity;

	@Mock
	GangTypeEntity gangTypeEntity;

	@Mock
	FactionEntity factionEntity;

	@Mock
	RaceEntity raceEntity;

	@Mock
	FighterTypeEntity fighterTypeEntity;

	@Mock
	TerritoryCategoryEntity territoryCategoryEntity;

	@Mock
	TerritoryTypeEntity territoryTypeEntity;

	@Mock
	SkillCategoryEntity skillCategoryEntity;

	@Mock
	SkillEntity skillEntity;

	@Mock
	ItemCategoryEntity itemCategoryEntity;

	@Mock
	ItemTypeEntity itemTypeEntity;

	@Mock
	GroupEntity wrongTypeGroupEntity;

	@Test
	public void viewRulesListChildren_ok() {
		assertEquals("Wrong rules returned", RULES_ID, instance.viewRulesListChildren(GROUP_ID).getId());
	}

	@Test
	public void viewRules_ok() {
		assertEquals("Wrong rules returned", RULES_ID, instance.viewRules(GROUP_ID).getId());
	}

	@Test
	public void listAllFactions_ok() {
		final List<FactionReferenceOutput> result = instance.listAllFactions(GROUP_ID);
		assertEquals("Wrong number of factions", 1, result.size());
		assertEquals("Wrong faction", FACTION_NAME, result.get(0).getName());
	}

	@Test
	public void viewGangType_ok() {
		final GangTypeOutput result = instance.viewGangType(GROUP_ID, GANG_TYPE_ID);
		assertEquals("Wrong gang type returned", GANG_TYPE_NAME, result.getName());
	}

	@Test
	public void viewFaction_ok() {
		final FactionOutput result = instance.viewFaction(GROUP_ID, FACTION_ID);
		assertEquals("Wrong faction returned", FACTION_NAME, result.getName());
	}

	@Test
	public void viewRace_ok() {
		final RaceOutput result = instance.viewRace(GROUP_ID, RACE_ID);
		assertEquals("Wrong race returned", RACE_NAME, result.getName());
	}

	@Test
	public void viewFighterType_ok() {
		final FighterTypeOutput result = instance.viewFighterType(GROUP_ID, GANG_TYPE_ID, RACE_ID, FIGHTER_TYPE_ID);
		assertEquals("Wrong fighter type returned", FIGHTER_TYPE_NAME, result.getName());
	}

	@Test
	public void viewTerritoryCategory_ok() {
		final TerritoryCategoryOutput result = instance.viewTerritoryCategory(GROUP_ID, TERRITORY_CATEGORY_ID);
		assertEquals("Wrong territory category returned", TERRITORY_CATEGORY_NAME, result.getName());
	}

	@Test
	public void viewTerritoryType_ok() {
		final TerritoryTypeOutput result = instance.viewTerritoryType(GROUP_ID, TERRITORY_TYPE_ID);
		assertEquals("Wrong territory type returned", TERRITORY_TYPE_NAME, result.getName());
	}

	@Test
	public void viewSkillCategory_ok() {
		final SkillCategoryOutput result = instance.viewSkillCategory(GROUP_ID, SKILL_CATEGORY_ID);
		assertEquals("Wrong skill category returned", SKILL_CATEGORY_NAME, result.getName());
	}

	@Test
	public void viewSkill_ok() {
		final SkillOutput result = instance.viewSkill(GROUP_ID, SKILL_ID);
		assertEquals("Wrong skill returned", SKILL_NAME, result.getName());
	}

	@Test
	public void viewItemCategory_ok() {
		final ItemCategoryOutput result = instance.viewItemCategory(GROUP_ID, ITEM_CATEGORY_ID);
		assertEquals("Wrong item category returned", ITEM_CATEGORY_NAME, result.getName());
	}

	@Test
	public void viewItemType_ok() {
		final ItemTypeOutput result = instance.viewItemType(GROUP_ID, ITEM_TYPE_ID);
		assertEquals("Wrong item type returned", ITEM_TYPE_NAME, result.getName());
	}

	@Before
	public void setupRules() {
		// Set up the rules entity
		when(rulesEntity.getId()).thenReturn(RULES_ID);
		when(rulesEntity.getGroup()).thenReturn(groupEntity);
		// Set up the gang type entity
		when(rulesEntity.getGangTypes()).thenReturn(new HashSet<GangTypeEntity>());
		when(gangTypeEntity.getId()).thenReturn(GANG_TYPE_ID);
		when(gangTypeEntity.getName()).thenReturn(GANG_TYPE_NAME);
		when(gangTypeEntity.getRules()).thenReturn(rulesEntity);
		rulesEntity.getGangTypes().add(gangTypeEntity);
		when(gangTypeRepository.load(GROUP_ID, GANG_TYPE_ID)).thenReturn(gangTypeEntity);
		// Set up the faction entity
		when(gangTypeEntity.getFactions()).thenReturn(new HashSet<FactionEntity>());
		when(factionEntity.getId()).thenReturn(FACTION_ID);
		when(factionEntity.getName()).thenReturn(FACTION_NAME);
		when(factionEntity.getGangType()).thenReturn(gangTypeEntity);
		gangTypeEntity.getFactions().add(factionEntity);
		when(factionRepository.load(GROUP_ID, FACTION_ID)).thenReturn(factionEntity);
		// Set up the race entity
		when(gangTypeEntity.getRaces()).thenReturn(new HashSet<RaceEntity>());
		when(raceEntity.getId()).thenReturn(RACE_ID);
		when(raceEntity.getName()).thenReturn(RACE_NAME);
		when(raceEntity.getGangType()).thenReturn(gangTypeEntity);
		gangTypeEntity.getRaces().add(raceEntity);
		when(raceRepository.load(GROUP_ID, RACE_ID)).thenReturn(raceEntity);
		// Set up the fighter type entity
		when(raceEntity.getFighterTypes()).thenReturn(new HashSet<FighterTypeEntity>());
		when(fighterTypeEntity.getId()).thenReturn(FIGHTER_TYPE_ID);
		when(fighterTypeEntity.getName()).thenReturn(FIGHTER_TYPE_NAME);
		when(fighterTypeEntity.getRace()).thenReturn(raceEntity);
		raceEntity.getFighterTypes().add(fighterTypeEntity);
		when(fighterTypeRepository.load(GROUP_ID, GANG_TYPE_ID, RACE_ID, FIGHTER_TYPE_ID)).thenReturn(fighterTypeEntity);
		// Set up the territory category entity
		when(rulesEntity.getTerritoryCategories()).thenReturn(new HashSet<TerritoryCategoryEntity>());
		when(territoryCategoryEntity.getId()).thenReturn(TERRITORY_CATEGORY_ID);
		when(territoryCategoryEntity.getName()).thenReturn(TERRITORY_CATEGORY_NAME);
		when(territoryCategoryEntity.getRules()).thenReturn(rulesEntity);
		rulesEntity.getTerritoryCategories().add(territoryCategoryEntity);
		when(territoryCategoryRepository.load(GROUP_ID, TERRITORY_CATEGORY_ID)).thenReturn(territoryCategoryEntity);
		// Set up the territory type entity
		when(territoryCategoryEntity.getTerritoryTypes()).thenReturn(new HashSet<TerritoryTypeEntity>());
		when(territoryTypeEntity.getId()).thenReturn(TERRITORY_TYPE_ID);
		when(territoryTypeEntity.getName()).thenReturn(TERRITORY_TYPE_NAME);
		when(territoryTypeEntity.getTerritoryCategory()).thenReturn(territoryCategoryEntity);
		territoryCategoryEntity.getTerritoryTypes().add(territoryTypeEntity);
		when(territoryTypeRepository.load(GROUP_ID, TERRITORY_TYPE_ID)).thenReturn(territoryTypeEntity);
		// Set up the skill category entity
		when(rulesEntity.getSkillCategories()).thenReturn(new HashSet<SkillCategoryEntity>());
		when(skillCategoryEntity.getId()).thenReturn(SKILL_CATEGORY_ID);
		when(skillCategoryEntity.getName()).thenReturn(SKILL_CATEGORY_NAME);
		when(skillCategoryEntity.getRules()).thenReturn(rulesEntity);
		rulesEntity.getSkillCategories().add(skillCategoryEntity);
		when(skillCategoryRepository.load(GROUP_ID, SKILL_CATEGORY_ID)).thenReturn(skillCategoryEntity);
		// Set up the skill entity
		when(skillCategoryEntity.getSkills()).thenReturn(new HashSet<SkillEntity>());
		when(skillEntity.getId()).thenReturn(SKILL_ID);
		when(skillEntity.getName()).thenReturn(SKILL_NAME);
		when(skillEntity.getSkillCategory()).thenReturn(skillCategoryEntity);
		skillCategoryEntity.getSkills().add(skillEntity);
		when(skillRepository.load(GROUP_ID, SKILL_ID)).thenReturn(skillEntity);
		// Set up the item category entity
		when(rulesEntity.getItemCategories()).thenReturn(new HashSet<ItemCategoryEntity>());
		when(itemCategoryEntity.getId()).thenReturn(ITEM_CATEGORY_ID);
		when(itemCategoryEntity.getName()).thenReturn(ITEM_CATEGORY_NAME);
		when(itemCategoryEntity.getRules()).thenReturn(rulesEntity);
		rulesEntity.getItemCategories().add(itemCategoryEntity);
		when(itemCategoryRepository.load(GROUP_ID, ITEM_CATEGORY_ID)).thenReturn(itemCategoryEntity);
		// Set up the item type entity
		when(itemCategoryEntity.getItemTypes()).thenReturn(new HashSet<ItemTypeEntity>());
		when(itemTypeEntity.getId()).thenReturn(ITEM_TYPE_ID);
		when(itemTypeEntity.getName()).thenReturn(ITEM_TYPE_NAME);
		when(itemTypeEntity.getItemCategory()).thenReturn(itemCategoryEntity);
		itemCategoryEntity.getItemTypes().add(itemTypeEntity);
		when(itemTypeRepository.load(GROUP_ID, ITEM_TYPE_ID)).thenReturn(itemTypeEntity);
	}

	@Before
	public void setupGroups() {
		when(groupEntity.getId()).thenReturn(GROUP_ID);
		when(groupEntity.getWh40kSkirmishRules()).thenReturn(rulesEntity);
		when(rulesRepository.load(GROUP_ID)).thenReturn(rulesEntity);
		when(wrongTypeGroupEntity.getId()).thenReturn(WRONG_TYPE_GROUP_ID);
	}

	@Before
	public void setupInstance() {
		instance = new RulesService();
		instance.groupService = groupService;
		instance.rulesRepository = rulesRepository;
		instance.skillCategoryRepository = skillCategoryRepository;
		instance.skillRepository = skillRepository;
		instance.itemCategoryRepository = itemCategoryRepository;
		instance.itemTypeRepository = itemTypeRepository;
		instance.territoryCategoryRepository = territoryCategoryRepository;
		instance.territoryTypeRepository = territoryTypeRepository;
		instance.gangTypeRepository = gangTypeRepository;
		instance.factionRepository = factionRepository;
		instance.raceRepository = raceRepository;
		instance.fighterTypeRepository = fighterTypeRepository;
	}
}

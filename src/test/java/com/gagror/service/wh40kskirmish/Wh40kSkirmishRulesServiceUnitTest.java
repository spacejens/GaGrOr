package com.gagror.service.wh40kskirmish;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
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

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishRulesServiceUnitTest {

	private static final Long GROUP_ID = 123L;
	private static final Long RULES_ID = 1L;
	private static final Long WRONG_TYPE_GROUP_ID = 456L;
	private static final Long GANG_TYPE_ID = 7L;
	private static final Long WRONG_GANG_TYPE_ID = 765L;
	private static final String GANG_TYPE_NAME = "Gang type";
	private static final Long FACTION_ID = 9L;
	private static final Long WRONG_FACTION_ID = 75783L;
	private static final String FACTION_NAME = "Faction";
	private static final Long RACE_ID = 113L;
	private static final Long WRONG_RACE_ID = 1543L;
	private static final String RACE_NAME = "Race";
	private static final Long FIGHTER_TYPE_ID = 457L;
	private static final Long WRONG_FIGHTER_TYPE_ID = 46378L;
	private static final String FIGHTER_TYPE_NAME = "Fighter type";
	private static final Long TERRITORY_CATEGORY_ID = 67L;
	private static final Long WRONG_TERRITORY_CATEGORY_ID = 7348L;
	private static final String TERRITORY_CATEGORY_NAME = "Territory category";
	private static final Long TERRITORY_TYPE_ID = 983L;
	private static final Long WRONG_TERRITORY_TYPE_ID = 21711L;
	private static final String TERRITORY_TYPE_NAME = "Territory type";
	private static final Long SKILL_CATEGORY_ID = 74154L;
	private static final Long WRONG_SKILL_CATEGORY_ID = 4664L;
	private static final String SKILL_CATEGORY_NAME = "Skill category";
	private static final Long SKILL_ID = 9998L;
	private static final Long WRONG_SKILL_ID = 13213L;
	private static final String SKILL_NAME = "Skill";
	private static final Long ITEM_CATEGORY_ID = 8986L;
	private static final Long WRONG_ITEM_CATEGORY_ID = 1231658L;
	private static final String ITEM_CATEGORY_NAME = "Item category";
	private static final Long ITEM_TYPE_ID = 165L;
	private static final Long WRONG_ITEM_TYPE_ID = 16514L;
	private static final String ITEM_TYPE_NAME = "Item type";

	Wh40kSkirmishRulesService instance;

	@Mock
	GroupService groupService;

	@Mock
	GroupEntity groupEntity;

	@Mock
	Wh40kSkirmishRulesEntity rulesEntity;

	@Mock
	Wh40kSkirmishGangTypeEntity gangTypeEntity;

	@Mock
	Wh40kSkirmishFactionEntity factionEntity;

	@Mock
	Wh40kSkirmishRaceEntity raceEntity;

	@Mock
	Wh40kSkirmishFighterTypeEntity fighterTypeEntity;

	@Mock
	Wh40kSkirmishTerritoryCategoryEntity territoryCategoryEntity;

	@Mock
	Wh40kSkirmishTerritoryTypeEntity territoryTypeEntity;

	@Mock
	Wh40kSkirmishSkillCategoryEntity skillCategoryEntity;

	@Mock
	Wh40kSkirmishSkillEntity skillEntity;

	@Mock
	Wh40kSkirmishItemCategoryEntity itemCategoryEntity;

	@Mock
	Wh40kSkirmishItemTypeEntity itemTypeEntity;

	@Mock
	GroupEntity wrongTypeGroupEntity;

	@Test
	public void viewRules_ok() {
		assertEquals("Wrong rules returned", RULES_ID, instance.viewRules(GROUP_ID).getId());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewRules_wrongType() {
		instance.viewRules(WRONG_TYPE_GROUP_ID);
	}

	@Test
	public void editRules_ok() {
		assertEquals("Wrong rules returned", RULES_ID, instance.editRules(GROUP_ID).getId());
	}

	@Test(expected=IllegalArgumentException.class)
	public void editRules_wrongType() {
		instance.editRules(WRONG_TYPE_GROUP_ID);
	}

	@Test
	public void viewGangType_ok() {
		final Wh40kSkirmishGangTypeOutput result = instance.viewGangType(GROUP_ID, GANG_TYPE_ID);
		assertEquals("Wrong gang type returned", GANG_TYPE_NAME, result.getName());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewGangType_notFound() {
		instance.viewGangType(GROUP_ID, WRONG_GANG_TYPE_ID);
	}

	@Test
	public void viewFaction_ok() {
		final Wh40kSkirmishFactionOutput result = instance.viewFaction(GROUP_ID, GANG_TYPE_ID, FACTION_ID);
		assertEquals("Wrong faction returned", FACTION_NAME, result.getName());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewFaction_notFound() {
		instance.viewFaction(GROUP_ID, GANG_TYPE_ID, WRONG_FACTION_ID);
	}

	@Test
	public void viewRace_ok() {
		final Wh40kSkirmishRaceOutput result = instance.viewRace(GROUP_ID, GANG_TYPE_ID, RACE_ID);
		assertEquals("Wrong race returned", RACE_NAME, result.getName());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewRace_notFound() {
		instance.viewRace(GROUP_ID, GANG_TYPE_ID, WRONG_RACE_ID);
	}

	@Test
	public void viewFighterType_ok() {
		final Wh40kSkirmishFighterTypeOutput result = instance.viewFighterType(GROUP_ID, GANG_TYPE_ID, RACE_ID, FIGHTER_TYPE_ID);
		assertEquals("Wrong fighter type returned", FIGHTER_TYPE_NAME, result.getName());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewFighterType_notFound() {
		instance.viewFighterType(GROUP_ID, GANG_TYPE_ID, RACE_ID, WRONG_FIGHTER_TYPE_ID);
	}

	@Test
	public void viewTerritoryCategory_ok() {
		final Wh40kSkirmishTerritoryCategoryOutput result = instance.viewTerritoryCategory(GROUP_ID, TERRITORY_CATEGORY_ID);
		assertEquals("Wrong territory category returned", TERRITORY_CATEGORY_NAME, result.getName());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewTerritoryCategory_notFound() {
		instance.viewTerritoryCategory(GROUP_ID, WRONG_TERRITORY_CATEGORY_ID);
	}

	@Test
	public void viewTerritoryType_ok() {
		final Wh40kSkirmishTerritoryTypeOutput result = instance.viewTerritoryType(GROUP_ID, TERRITORY_CATEGORY_ID, TERRITORY_TYPE_ID);
		assertEquals("Wrong territory type returned", TERRITORY_TYPE_NAME, result.getName());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewTerritoryType_notFound() {
		instance.viewTerritoryType(GROUP_ID, TERRITORY_CATEGORY_ID, WRONG_TERRITORY_TYPE_ID);
	}

	@Test
	public void viewSkillCategory_ok() {
		final Wh40kSkirmishSkillCategoryOutput result = instance.viewSkillCategory(GROUP_ID, SKILL_CATEGORY_ID);
		assertEquals("Wrong skill category returned", SKILL_CATEGORY_NAME, result.getName());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewSkillCategory_notFound() {
		instance.viewSkillCategory(GROUP_ID, WRONG_SKILL_CATEGORY_ID);
	}

	@Test
	public void viewSkill_ok() {
		final Wh40kSkirmishSkillOutput result = instance.viewSkill(GROUP_ID, SKILL_CATEGORY_ID, SKILL_ID);
		assertEquals("Wrong skill returned", SKILL_NAME, result.getName());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewSkill_notFound() {
		instance.viewSkill(GROUP_ID, SKILL_CATEGORY_ID, WRONG_SKILL_ID);
	}

	@Test
	public void viewItemCategory_ok() {
		final Wh40kSkirmishItemCategoryOutput result = instance.viewItemCategory(GROUP_ID, ITEM_CATEGORY_ID);
		assertEquals("Wrong item category returned", ITEM_CATEGORY_NAME, result.getName());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewItemCategory_notFound() {
		instance.viewItemCategory(GROUP_ID, WRONG_ITEM_CATEGORY_ID);
	}

	@Test
	public void viewItemType_ok() {
		final Wh40kSkirmishItemTypeOutput result = instance.viewItemType(GROUP_ID, ITEM_CATEGORY_ID, ITEM_TYPE_ID);
		assertEquals("Wrong item type returned", ITEM_TYPE_NAME, result.getName());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewItemType_notFound() {
		instance.viewItemType(GROUP_ID, ITEM_CATEGORY_ID, WRONG_ITEM_TYPE_ID);
	}

	@Before
	public void setupRules() {
		// Set up the rules entity
		when(rulesEntity.getId()).thenReturn(RULES_ID);
		when(rulesEntity.getGroup()).thenReturn(groupEntity);
		// Set up the gang type entity
		when(rulesEntity.getGangTypes()).thenReturn(new HashSet<Wh40kSkirmishGangTypeEntity>());
		when(gangTypeEntity.getId()).thenReturn(GANG_TYPE_ID);
		when(gangTypeEntity.getName()).thenReturn(GANG_TYPE_NAME);
		when(gangTypeEntity.getRules()).thenReturn(rulesEntity);
		rulesEntity.getGangTypes().add(gangTypeEntity);
		// Set up the faction entity
		when(gangTypeEntity.getFactions()).thenReturn(new HashSet<Wh40kSkirmishFactionEntity>());
		when(factionEntity.getId()).thenReturn(FACTION_ID);
		when(factionEntity.getName()).thenReturn(FACTION_NAME);
		when(factionEntity.getGangType()).thenReturn(gangTypeEntity);
		gangTypeEntity.getFactions().add(factionEntity);
		// Set up the race entity
		when(gangTypeEntity.getRaces()).thenReturn(new HashSet<Wh40kSkirmishRaceEntity>());
		when(raceEntity.getId()).thenReturn(RACE_ID);
		when(raceEntity.getName()).thenReturn(RACE_NAME);
		when(raceEntity.getGangType()).thenReturn(gangTypeEntity);
		gangTypeEntity.getRaces().add(raceEntity);
		// Set up the fighter type entity
		when(raceEntity.getFighterTypes()).thenReturn(new HashSet<Wh40kSkirmishFighterTypeEntity>());
		when(fighterTypeEntity.getId()).thenReturn(FIGHTER_TYPE_ID);
		when(fighterTypeEntity.getName()).thenReturn(FIGHTER_TYPE_NAME);
		when(fighterTypeEntity.getRace()).thenReturn(raceEntity);
		raceEntity.getFighterTypes().add(fighterTypeEntity);
		// Set up the territory category entity
		when(rulesEntity.getTerritoryCategories()).thenReturn(new HashSet<Wh40kSkirmishTerritoryCategoryEntity>());
		when(territoryCategoryEntity.getId()).thenReturn(TERRITORY_CATEGORY_ID);
		when(territoryCategoryEntity.getName()).thenReturn(TERRITORY_CATEGORY_NAME);
		when(territoryCategoryEntity.getRules()).thenReturn(rulesEntity);
		rulesEntity.getTerritoryCategories().add(territoryCategoryEntity);
		// Set up the territory type entity
		when(territoryCategoryEntity.getTerritoryTypes()).thenReturn(new HashSet<Wh40kSkirmishTerritoryTypeEntity>());
		when(territoryTypeEntity.getId()).thenReturn(TERRITORY_TYPE_ID);
		when(territoryTypeEntity.getName()).thenReturn(TERRITORY_TYPE_NAME);
		when(territoryTypeEntity.getTerritoryCategory()).thenReturn(territoryCategoryEntity);
		territoryCategoryEntity.getTerritoryTypes().add(territoryTypeEntity);
		// Set up the skill category entity
		when(rulesEntity.getSkillCategories()).thenReturn(new HashSet<Wh40kSkirmishSkillCategoryEntity>());
		when(skillCategoryEntity.getId()).thenReturn(SKILL_CATEGORY_ID);
		when(skillCategoryEntity.getName()).thenReturn(SKILL_CATEGORY_NAME);
		when(skillCategoryEntity.getRules()).thenReturn(rulesEntity);
		rulesEntity.getSkillCategories().add(skillCategoryEntity);
		// Set up the skill entity
		when(skillCategoryEntity.getSkills()).thenReturn(new HashSet<Wh40kSkirmishSkillEntity>());
		when(skillEntity.getId()).thenReturn(SKILL_ID);
		when(skillEntity.getName()).thenReturn(SKILL_NAME);
		when(skillEntity.getSkillCategory()).thenReturn(skillCategoryEntity);
		skillCategoryEntity.getSkills().add(skillEntity);
		// Set up the item category entity
		when(rulesEntity.getItemCategories()).thenReturn(new HashSet<Wh40kSkirmishItemCategoryEntity>());
		when(itemCategoryEntity.getId()).thenReturn(ITEM_CATEGORY_ID);
		when(itemCategoryEntity.getName()).thenReturn(ITEM_CATEGORY_NAME);
		when(itemCategoryEntity.getRules()).thenReturn(rulesEntity);
		rulesEntity.getItemCategories().add(itemCategoryEntity);
		// Set up the item type entity
		when(itemCategoryEntity.getItemTypes()).thenReturn(new HashSet<Wh40kSkirmishItemTypeEntity>());
		when(itemTypeEntity.getId()).thenReturn(ITEM_TYPE_ID);
		when(itemTypeEntity.getName()).thenReturn(ITEM_TYPE_NAME);
		when(itemTypeEntity.getItemCategory()).thenReturn(itemCategoryEntity);
		itemCategoryEntity.getItemTypes().add(itemTypeEntity);
	}

	@Before
	public void setupGroups() {
		mockGroup(groupEntity, GROUP_ID);
		when(groupEntity.getWh40kSkirmishRules()).thenReturn(rulesEntity);
		mockGroup(wrongTypeGroupEntity, WRONG_TYPE_GROUP_ID);
	}

	private void mockGroup(final GroupEntity group, final Long id) {
		when(group.getId()).thenReturn(id);
		when(groupService.loadGroup(id)).thenReturn(group);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishRulesService();
		instance.groupService = groupService;
	}
}

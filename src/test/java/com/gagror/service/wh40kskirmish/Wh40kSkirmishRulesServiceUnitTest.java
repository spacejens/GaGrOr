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

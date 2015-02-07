package com.gagror.service.wh40kskirmish.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.gagror.AddError;
import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceInput;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceRepository;

@RunWith(MockitoJUnitRunner.class)
public class RacePersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long GANG_TYPE_ID = 5789L;
	private static final String FORM_RACE_NAME = "Race form";
	private static final int FORM_RACE_MOVEMENT = 1;
	private static final int FORM_RACE_WEAPON_SKILL = 2;
	private static final int FORM_RACE_BALLISTIC_SKILL = 3;
	private static final int FORM_RACE_STRENGTH = 4;
	private static final int FORM_RACE_TOUGHNESS = 5;
	private static final int FORM_RACE_WOUNDS = 6;
	private static final int FORM_RACE_INITIATIVE = 7;
	private static final int FORM_RACE_ATTACKS = 8;
	private static final int FORM_RACE_LEADERSHIP = 9;
	private static final Long DB_RACE_ID = 11L;
	private static final String DB_RACE_NAME = "Race DB";
	private static final Long DB_RACE_VERSION = 5L;
	private static final Long DB_FIGHTERTYPE_ID = 6473L;

	RacePersister instance;

	@Mock
	RaceInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GangTypeRepository gangTypeRepository;

	@Mock
	RaceRepository raceRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	GangTypeEntity gangType;

	@Mock
	RaceEntity race;

	@Mock
	FighterTypeEntity fighterType;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		final ArgumentCaptor<RaceEntity> savedRace = ArgumentCaptor.forClass(RaceEntity.class);
		verify(raceRepository).persist(savedRace.capture());
		assertEquals("Wrong name", FORM_RACE_NAME, savedRace.getValue().getName());
		assertEquals("Wrong movement", FORM_RACE_MOVEMENT, savedRace.getValue().getMaxMovement());
		assertEquals("Wrong weapon skill", FORM_RACE_WEAPON_SKILL, savedRace.getValue().getMaxWeaponSkill());
		assertEquals("Wrong ballistic skill", FORM_RACE_BALLISTIC_SKILL, savedRace.getValue().getMaxBallisticSkill());
		assertEquals("Wrong strength", FORM_RACE_STRENGTH, savedRace.getValue().getMaxStrength());
		assertEquals("Wrong toughness", FORM_RACE_TOUGHNESS, savedRace.getValue().getMaxToughness());
		assertEquals("Wrong wounds", FORM_RACE_WOUNDS, savedRace.getValue().getMaxWounds());
		assertEquals("Wrong initiative", FORM_RACE_INITIATIVE, savedRace.getValue().getMaxInitiative());
		assertEquals("Wrong attacks", FORM_RACE_ATTACKS, savedRace.getValue().getMaxAttacks());
		assertEquals("Wrong leadership", FORM_RACE_LEADERSHIP, savedRace.getValue().getMaxLeadership());
		assertTrue("Not added to gang type", gangType.getRaces().contains(savedRace.getValue()));
	}

	@Test
	public void save_new_nameNotUnique() {
		whenAnotherRaceWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(raceRepository, never()).persist(any(RaceEntity.class));
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(raceRepository, never()).persist(any(RaceEntity.class));
	}

	@Test(expected=DataNotFoundException.class)
	public void save_new_gangTypeNotFound() {
		doThrow(DataNotFoundException.class).when(gangTypeRepository).load(GROUP_ID, GANG_TYPE_ID);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenRaceExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		verify(raceRepository, never()).persist(any(RaceEntity.class));
		verify(race).setName(FORM_RACE_NAME);
		verify(race).setMaxMovement(FORM_RACE_MOVEMENT);
		verify(race).setMaxWeaponSkill(FORM_RACE_WEAPON_SKILL);
		verify(race).setMaxBallisticSkill(FORM_RACE_BALLISTIC_SKILL);
		verify(race).setMaxStrength(FORM_RACE_STRENGTH);
		verify(race).setMaxToughness(FORM_RACE_TOUGHNESS);
		verify(race).setMaxWounds(FORM_RACE_WOUNDS);
		verify(race).setMaxInitiative(FORM_RACE_INITIATIVE);
		verify(race).setMaxAttacks(FORM_RACE_ATTACKS);
		verify(race).setMaxLeadership(FORM_RACE_LEADERSHIP);
	}

	@Test
	public void save_existing_nameNotUnique() {
		whenRaceExists();
		whenAnotherRaceWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setName(anyString());
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_existing_minMovement() {
		whenRaceExists();
		when(form.getMaxMovement()).thenReturn(FORM_RACE_MOVEMENT - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setMaxMovement(anyInt());
		verify(form).addErrorMaxBelowStartingMovement(bindingResult, FORM_RACE_MOVEMENT);
	}

	@Test
	public void save_existing_minWeaponSkill() {
		whenRaceExists();
		when(form.getMaxWeaponSkill()).thenReturn(FORM_RACE_WEAPON_SKILL - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setMaxWeaponSkill(anyInt());
		verify(form).addErrorMaxBelowStartingWeaponSkill(bindingResult, FORM_RACE_WEAPON_SKILL);
	}

	@Test
	public void save_existing_minBallisticSkill() {
		whenRaceExists();
		when(form.getMaxBallisticSkill()).thenReturn(FORM_RACE_BALLISTIC_SKILL - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setMaxBallisticSkill(anyInt());
		verify(form).addErrorMaxBelowStartingBallisticSkill(bindingResult, FORM_RACE_BALLISTIC_SKILL);
	}

	@Test
	public void save_existing_minStrength() {
		whenRaceExists();
		when(form.getMaxStrength()).thenReturn(FORM_RACE_STRENGTH - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setMaxStrength(anyInt());
		verify(form).addErrorMaxBelowStartingStrength(bindingResult, FORM_RACE_STRENGTH);
	}

	@Test
	public void save_existing_minToughness() {
		whenRaceExists();
		when(form.getMaxToughness()).thenReturn(FORM_RACE_TOUGHNESS - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setMaxToughness(anyInt());
		verify(form).addErrorMaxBelowStartingToughness(bindingResult, FORM_RACE_TOUGHNESS);
	}

	@Test
	public void save_existing_minWounds() {
		whenRaceExists();
		when(form.getMaxWounds()).thenReturn(FORM_RACE_WOUNDS - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setMaxWounds(anyInt());
		verify(form).addErrorMaxBelowStartingWounds(bindingResult, FORM_RACE_WOUNDS);
	}

	@Test
	public void save_existing_minInitiative() {
		whenRaceExists();
		when(form.getMaxInitiative()).thenReturn(FORM_RACE_INITIATIVE - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setMaxInitiative(anyInt());
		verify(form).addErrorMaxBelowStartingInitiative(bindingResult, FORM_RACE_INITIATIVE);
	}

	@Test
	public void save_existing_minAttacks() {
		whenRaceExists();
		when(form.getMaxAttacks()).thenReturn(FORM_RACE_ATTACKS - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setMaxAttacks(anyInt());
		verify(form).addErrorMaxBelowStartingAttacks(bindingResult, FORM_RACE_ATTACKS);
	}

	@Test
	public void save_existing_minLeadership() {
		whenRaceExists();
		when(form.getMaxLeadership()).thenReturn(FORM_RACE_LEADERSHIP - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setMaxLeadership(anyInt());
		verify(form).addErrorMaxBelowStartingLeadership(bindingResult, FORM_RACE_LEADERSHIP);
	}

	@Test
	public void save_existing_bindingError() {
		whenRaceExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setName(anyString());
		verify(raceRepository, never()).persist(any(RaceEntity.class));
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_gangTypeNotFound() {
		whenRaceExists();
		doThrow(DataNotFoundException.class).when(gangTypeRepository).load(GROUP_ID, GANG_TYPE_ID);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_notFoundInGangType() {
		whenRaceExists();
		gangType.getRaces().remove(race);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenRaceExists();
		when(form.getVersion()).thenReturn(DB_RACE_VERSION - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(race, never()).setName(anyString());
	}

	protected void whenAnotherRaceWithSameNameExists() {
		final GangTypeEntity anotherGangType = mock(GangTypeEntity.class);
		when(anotherGangType.getId()).thenReturn(GANG_TYPE_ID + 1);
		when(anotherGangType.getRaces()).thenReturn(new HashSet<RaceEntity>());
		rules.getGangTypes().add(anotherGangType);
		final RaceEntity anotherRace = mock(RaceEntity.class);
		when(anotherRace.getId()).thenReturn(DB_RACE_ID + 1);
		when(anotherRace.getName()).thenReturn(FORM_RACE_NAME);
		anotherGangType.getRaces().add(anotherRace);
	}

	protected void whenRaceExists() {
		when(form.getId()).thenReturn(DB_RACE_ID);
		when(form.getVersion()).thenReturn(DB_RACE_VERSION);
		when(race.getId()).thenReturn(DB_RACE_ID);
		when(race.getVersion()).thenReturn(DB_RACE_VERSION);
		when(race.getName()).thenReturn(DB_RACE_NAME);
		when(race.getGangType()).thenReturn(gangType);
		gangType.getRaces().add(race);
		// Create a fighter type
		when(race.getFighterTypes()).thenReturn(new HashSet<FighterTypeEntity>());
		when(fighterType.getId()).thenReturn(DB_FIGHTERTYPE_ID);
		when(fighterType.getRace()).thenReturn(race);
		when(fighterType.getStartingMovement()).thenReturn(FORM_RACE_MOVEMENT);
		when(fighterType.getStartingWeaponSkill()).thenReturn(FORM_RACE_WEAPON_SKILL);
		when(fighterType.getStartingBallisticSkill()).thenReturn(FORM_RACE_BALLISTIC_SKILL);
		when(fighterType.getStartingStrength()).thenReturn(FORM_RACE_STRENGTH);
		when(fighterType.getStartingToughness()).thenReturn(FORM_RACE_TOUGHNESS);
		when(fighterType.getStartingWounds()).thenReturn(FORM_RACE_WOUNDS);
		when(fighterType.getStartingInitiative()).thenReturn(FORM_RACE_INITIATIVE);
		when(fighterType.getStartingAttacks()).thenReturn(FORM_RACE_ATTACKS);
		when(fighterType.getStartingLeadership()).thenReturn(FORM_RACE_LEADERSHIP);
		race.getFighterTypes().add(fighterType);
	}

	@Before
	public void setupForm() {
		when(form.getId()).thenReturn(null);
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getGangTypeId()).thenReturn(GANG_TYPE_ID);
		when(form.getName()).thenReturn(FORM_RACE_NAME);
		when(form.getMaxMovement()).thenReturn(FORM_RACE_MOVEMENT);
		when(form.getMaxWeaponSkill()).thenReturn(FORM_RACE_WEAPON_SKILL);
		when(form.getMaxBallisticSkill()).thenReturn(FORM_RACE_BALLISTIC_SKILL);
		when(form.getMaxStrength()).thenReturn(FORM_RACE_STRENGTH);
		when(form.getMaxToughness()).thenReturn(FORM_RACE_TOUGHNESS);
		when(form.getMaxWounds()).thenReturn(FORM_RACE_WOUNDS);
		when(form.getMaxInitiative()).thenReturn(FORM_RACE_INITIATIVE);
		when(form.getMaxAttacks()).thenReturn(FORM_RACE_ATTACKS);
		when(form.getMaxLeadership()).thenReturn(FORM_RACE_LEADERSHIP);
		AddError.to(bindingResult).when(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
		AddError.to(bindingResult).when(form).addErrorSimultaneuosEdit(bindingResult);
		AddError.to(bindingResult).when(form).addErrorMaxBelowStartingMovement(bindingResult, FORM_RACE_MOVEMENT);
		AddError.to(bindingResult).when(form).addErrorMaxBelowStartingWeaponSkill(bindingResult, FORM_RACE_WEAPON_SKILL);
		AddError.to(bindingResult).when(form).addErrorMaxBelowStartingBallisticSkill(bindingResult, FORM_RACE_BALLISTIC_SKILL);
		AddError.to(bindingResult).when(form).addErrorMaxBelowStartingStrength(bindingResult, FORM_RACE_STRENGTH);
		AddError.to(bindingResult).when(form).addErrorMaxBelowStartingToughness(bindingResult, FORM_RACE_TOUGHNESS);
		AddError.to(bindingResult).when(form).addErrorMaxBelowStartingWounds(bindingResult, FORM_RACE_WOUNDS);
		AddError.to(bindingResult).when(form).addErrorMaxBelowStartingInitiative(bindingResult, FORM_RACE_INITIATIVE);
		AddError.to(bindingResult).when(form).addErrorMaxBelowStartingAttacks(bindingResult, FORM_RACE_ATTACKS);
		AddError.to(bindingResult).when(form).addErrorMaxBelowStartingLeadership(bindingResult, FORM_RACE_LEADERSHIP);
	}

	@Before
	public void setupContext() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getGangTypes()).thenReturn(new HashSet<GangTypeEntity>());
		when(gangType.getId()).thenReturn(GANG_TYPE_ID);
		when(gangType.getRaces()).thenReturn(new HashSet<RaceEntity>());
		when(gangType.getRules()).thenReturn(rules);
		rules.getGangTypes().add(gangType);
		when(gangTypeRepository.load(GROUP_ID, GANG_TYPE_ID)).thenReturn(gangType);
	}

	@Before
	public void setupInstance() {
		instance = new RacePersister();
		instance.gangTypeRepository = gangTypeRepository;
		instance.raceRepository = raceRepository;
	}
}

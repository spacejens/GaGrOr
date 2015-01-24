package com.gagror.service.wh40kskirmish.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceEntity;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishFighterTypePersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long GANG_TYPE_ID = 5789L;
	private static final Long RACE_ID = 6724L;
	private static final String FORM_FIGHTERTYPE_NAME = "Fighter type form";
	private static final int FORM_FIGHTERTYPE_MOVEMENT = 1;
	private static final int FORM_FIGHTERTYPE_WEAPON_SKILL = 2;
	private static final int FORM_FIGHTERTYPE_BALLISTIC_SKILL = 3;
	private static final int FORM_FIGHTERTYPE_STRENGTH = 4;
	private static final int FORM_FIGHTERTYPE_TOUGHNESS = 5;
	private static final int FORM_FIGHTERTYPE_WOUNDS = 6;
	private static final int FORM_FIGHTERTYPE_INITIATIVE = 7;
	private static final int FORM_FIGHTERTYPE_ATTACKS = 8;
	private static final int FORM_FIGHTERTYPE_LEADERSHIP = 9;
	private static final Long DB_FIGHTERTYPE_ID = 11L;
	private static final String DB_FIGHTERTYPE_NAME = "Fighter type DB";
	private static final Long DB_FIGHTERTYPE_VERSION = 5L;

	Wh40kSkirmishFighterTypePersister instance;

	@Mock
	Wh40kSkirmishFighterTypeInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	Wh40kSkirmishFighterTypeRepository fighterTypeRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	Wh40kSkirmishGangTypeEntity gangType;

	@Mock
	Wh40kSkirmishRaceEntity race;

	@Mock
	Wh40kSkirmishFighterTypeEntity fighterType;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<Wh40kSkirmishFighterTypeEntity> savedFighterType = ArgumentCaptor.forClass(Wh40kSkirmishFighterTypeEntity.class);
		verify(fighterTypeRepository).save(savedFighterType.capture());
		assertEquals("Wrong name", FORM_FIGHTERTYPE_NAME, savedFighterType.getValue().getName());
		assertEquals("Wrong movement", FORM_FIGHTERTYPE_MOVEMENT, savedFighterType.getValue().getStartingMovement());
		assertEquals("Wrong weapon skill", FORM_FIGHTERTYPE_WEAPON_SKILL, savedFighterType.getValue().getStartingWeaponSkill());
		assertEquals("Wrong ballistic skill", FORM_FIGHTERTYPE_BALLISTIC_SKILL, savedFighterType.getValue().getStartingBallisticSkill());
		assertEquals("Wrong strength", FORM_FIGHTERTYPE_STRENGTH, savedFighterType.getValue().getStartingStrength());
		assertEquals("Wrong toughness", FORM_FIGHTERTYPE_TOUGHNESS, savedFighterType.getValue().getStartingToughness());
		assertEquals("Wrong wounds", FORM_FIGHTERTYPE_WOUNDS, savedFighterType.getValue().getStartingWounds());
		assertEquals("Wrong initiative", FORM_FIGHTERTYPE_INITIATIVE, savedFighterType.getValue().getStartingInitiative());
		assertEquals("Wrong attacks", FORM_FIGHTERTYPE_ATTACKS, savedFighterType.getValue().getStartingAttacks());
		assertEquals("Wrong leadership", FORM_FIGHTERTYPE_LEADERSHIP, savedFighterType.getValue().getStartingLeadership());
		assertTrue("Not added to gang type", race.getFighterTypes().contains(savedFighterType.getValue()));
	}

	@Test
	public void save_new_nameNotUnique() {
		whenAnotherFighterTypeWithSameNameExists();
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(Wh40kSkirmishFighterTypeEntity.class));
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(Wh40kSkirmishFighterTypeEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_new_gangTypeNotFound() {
		rules.getGangTypes().remove(gangType);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_new_raceNotFound() {
		gangType.getRaces().remove(race);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenFighterTypeExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		verify(fighterTypeRepository, never()).save(any(Wh40kSkirmishFighterTypeEntity.class));
		verify(fighterType).setName(FORM_FIGHTERTYPE_NAME);
		verify(fighterType).setStartingMovement(FORM_FIGHTERTYPE_MOVEMENT);
		verify(fighterType).setStartingWeaponSkill(FORM_FIGHTERTYPE_WEAPON_SKILL);
		verify(fighterType).setStartingBallisticSkill(FORM_FIGHTERTYPE_BALLISTIC_SKILL);
		verify(fighterType).setStartingStrength(FORM_FIGHTERTYPE_STRENGTH);
		verify(fighterType).setStartingToughness(FORM_FIGHTERTYPE_TOUGHNESS);
		verify(fighterType).setStartingWounds(FORM_FIGHTERTYPE_WOUNDS);
		verify(fighterType).setStartingInitiative(FORM_FIGHTERTYPE_INITIATIVE);
		verify(fighterType).setStartingAttacks(FORM_FIGHTERTYPE_ATTACKS);
		verify(fighterType).setStartingLeadership(FORM_FIGHTERTYPE_LEADERSHIP);
	}

	@Test
	public void save_existing_nameNotUnique() {
		whenFighterTypeExists();
		whenAnotherFighterTypeWithSameNameExists();
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterType, never()).setName(anyString());
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_existing_bindingError() {
		whenFighterTypeExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setName(anyString());
		verify(fighterTypeRepository, never()).save(any(Wh40kSkirmishFighterTypeEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_existing_wrongGroupType() {
		whenFighterTypeExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_gangTypeNotFound() {
		whenFighterTypeExists();
		rules.getGangTypes().remove(gangType);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_raceNotFound() {
		whenFighterTypeExists();
		gangType.getRaces().remove(race);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_notFoundInRace() {
		whenFighterTypeExists();
		race.getFighterTypes().remove(fighterType);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenFighterTypeExists();
		when(form.getVersion()).thenReturn(DB_FIGHTERTYPE_VERSION - 1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(race, never()).setName(anyString());
	}

	protected void whenAnotherFighterTypeWithSameNameExists() {
		final Wh40kSkirmishGangTypeEntity anotherGangType = mock(Wh40kSkirmishGangTypeEntity.class);
		when(anotherGangType.getId()).thenReturn(GANG_TYPE_ID + 1);
		when(anotherGangType.getRaces()).thenReturn(new HashSet<Wh40kSkirmishRaceEntity>());
		rules.getGangTypes().add(anotherGangType);
		final Wh40kSkirmishRaceEntity anotherRace = mock(Wh40kSkirmishRaceEntity.class);
		when(anotherRace.getId()).thenReturn(RACE_ID + 1);
		when(anotherRace.getFighterTypes()).thenReturn(new HashSet<Wh40kSkirmishFighterTypeEntity>());
		anotherGangType.getRaces().add(anotherRace);
		final Wh40kSkirmishFighterTypeEntity anotherFighterType = mock(Wh40kSkirmishFighterTypeEntity.class);
		when(anotherFighterType.getId()).thenReturn(DB_FIGHTERTYPE_ID + 1);
		when(anotherFighterType.getName()).thenReturn(FORM_FIGHTERTYPE_NAME);
		anotherRace.getFighterTypes().add(anotherFighterType);
	}

	protected void whenFighterTypeExists() {
		when(form.getId()).thenReturn(DB_FIGHTERTYPE_ID);
		when(form.getVersion()).thenReturn(DB_FIGHTERTYPE_VERSION);
		when(fighterType.getId()).thenReturn(DB_FIGHTERTYPE_ID);
		when(fighterType.getVersion()).thenReturn(DB_FIGHTERTYPE_VERSION);
		when(fighterType.getName()).thenReturn(DB_FIGHTERTYPE_NAME);
		when(fighterType.getRace()).thenReturn(race);
		race.getFighterTypes().add(fighterType);
	}

	@Before
	public void setupForm() {
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getGangTypeId()).thenReturn(GANG_TYPE_ID);
		when(form.getRaceId()).thenReturn(RACE_ID);
		when(form.getName()).thenReturn(FORM_FIGHTERTYPE_NAME);
		when(form.getStartingMovement()).thenReturn(FORM_FIGHTERTYPE_MOVEMENT);
		when(form.getStartingWeaponSkill()).thenReturn(FORM_FIGHTERTYPE_WEAPON_SKILL);
		when(form.getStartingBallisticSkill()).thenReturn(FORM_FIGHTERTYPE_BALLISTIC_SKILL);
		when(form.getStartingStrength()).thenReturn(FORM_FIGHTERTYPE_STRENGTH);
		when(form.getStartingToughness()).thenReturn(FORM_FIGHTERTYPE_TOUGHNESS);
		when(form.getStartingWounds()).thenReturn(FORM_FIGHTERTYPE_WOUNDS);
		when(form.getStartingInitiative()).thenReturn(FORM_FIGHTERTYPE_INITIATIVE);
		when(form.getStartingAttacks()).thenReturn(FORM_FIGHTERTYPE_ATTACKS);
		when(form.getStartingLeadership()).thenReturn(FORM_FIGHTERTYPE_LEADERSHIP);
	}

	@Before
	public void setupContext() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getGangTypes()).thenReturn(new HashSet<Wh40kSkirmishGangTypeEntity>());
		when(gangType.getId()).thenReturn(GANG_TYPE_ID);
		when(gangType.getRaces()).thenReturn(new HashSet<Wh40kSkirmishRaceEntity>());
		when(gangType.getRules()).thenReturn(rules);
		rules.getGangTypes().add(gangType);
		when(race.getId()).thenReturn(RACE_ID);
		when(race.getFighterTypes()).thenReturn(new HashSet<Wh40kSkirmishFighterTypeEntity>());
		when(race.getGangType()).thenReturn(gangType);
		gangType.getRaces().add(race);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishFighterTypePersister();
		instance.groupRepository = groupRepository;
		instance.fighterTypeRepository = fighterTypeRepository;
	}
}

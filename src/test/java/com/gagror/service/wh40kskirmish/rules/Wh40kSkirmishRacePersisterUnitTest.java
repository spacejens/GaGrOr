package com.gagror.service.wh40kskirmish.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceRepository;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishRacePersisterUnitTest {

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

	Wh40kSkirmishRacePersister instance;

	@Mock
	Wh40kSkirmishRaceInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	Wh40kSkirmishRaceRepository raceRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	Wh40kSkirmishGangTypeEntity gangType;

	@Mock
	Wh40kSkirmishRaceEntity race;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<Wh40kSkirmishRaceEntity> savedRace = ArgumentCaptor.forClass(Wh40kSkirmishRaceEntity.class);
		verify(raceRepository).save(savedRace.capture());
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
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(raceRepository, never()).save(any(Wh40kSkirmishRaceEntity.class));
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

	@Test
	public void save_existing_ok() {
		whenRaceExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		verify(raceRepository, never()).save(any(Wh40kSkirmishRaceEntity.class));
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
	public void save_existing_bindingError() {
		whenRaceExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setName(anyString());
		verify(raceRepository, never()).save(any(Wh40kSkirmishRaceEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_existing_wrongGroupType() {
		whenRaceExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_gangTypeNotFound() {
		whenRaceExists();
		rules.getGangTypes().remove(gangType);
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
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(race, never()).setName(anyString());
	}

	protected void whenRaceExists() {
		when(form.getId()).thenReturn(DB_RACE_ID);
		when(form.getVersion()).thenReturn(DB_RACE_VERSION);
		when(race.getId()).thenReturn(DB_RACE_ID);
		when(race.getVersion()).thenReturn(DB_RACE_VERSION);
		when(race.getName()).thenReturn(DB_RACE_NAME);
		when(race.getGangType()).thenReturn(gangType);
		gangType.getRaces().add(race);
	}

	@Before
	public void setupForm() {
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
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishRacePersister();
		instance.groupRepository = groupRepository;
		instance.raceRepository = raceRepository;
	}
}
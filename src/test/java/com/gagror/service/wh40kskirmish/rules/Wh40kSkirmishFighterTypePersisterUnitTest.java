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
		assertTrue("Not added to gang type", race.getFighterTypes().contains(savedFighterType.getValue()));
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

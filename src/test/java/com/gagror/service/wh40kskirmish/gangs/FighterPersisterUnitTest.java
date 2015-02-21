package com.gagror.service.wh40kskirmish.gangs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.gagror.AddError;
import com.gagror.data.wh40kskirmish.gangs.FighterEntity;
import com.gagror.data.wh40kskirmish.gangs.FighterInput;
import com.gagror.data.wh40kskirmish.gangs.FighterRepository;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;

@RunWith(MockitoJUnitRunner.class)
public class FighterPersisterUnitTest {

	private static final Long GROUP_ID = 1L;
	private static final Long GANG_ID = 2L;
	private static final Long FIGHTER_ID = 3L;
	private static final Long ANOTHER_GANG_ID = 4L;
	private static final Long ANOTHER_FIGHTER_ID = 5L;
	private static final String FIGHTER_NAME = "Fighter name";
	private static final Long FIGHTER_VERSION = 77L;

	FighterPersister instance;

	@Mock
	GangRepository gangRepository;

	@Mock
	FighterRepository fighterRepository;

	@Mock
	FighterInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	GangTypeEntity gangType;

	@Mock
	FactionEntity faction;

	@Mock
	GangEntity gang;

	@Mock
	FighterEntity fighter;

	@Mock
	GangEntity anotherGang;

	@Mock
	FighterEntity anotherFighter;

	@Test
	public void save_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(fighter).setName(FIGHTER_NAME);
	}

	@Test
	public void save_nameNotUnique() {
		when(anotherFighter.getName()).thenReturn(FIGHTER_NAME);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighter, never()).setName(anyString());
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighter, never()).setName(anyString());
	}

	@Test
	public void save_simultaneousEdit() {
		when(form.getVersion()).thenReturn(FIGHTER_VERSION - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighter, never()).setName(anyString());
		verify(form).addErrorSimultaneuosEdit(bindingResult);
	}

	@Before
	public void setupForm() {
		when(form.getId()).thenReturn(FIGHTER_ID);
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getGangId()).thenReturn(GANG_ID);
		when(form.getVersion()).thenReturn(FIGHTER_VERSION);
		when(form.getName()).thenReturn(FIGHTER_NAME);
		AddError.to(bindingResult).when(form).addErrorSimultaneuosEdit(bindingResult);
		AddError.to(bindingResult).when(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Before
	public void setupContext() {
		when(rules.getGangTypes()).thenReturn(Collections.singleton(gangType));
		when(gangType.getFactions()).thenReturn(Collections.singleton(faction));
		when(gang.getId()).thenReturn(GANG_ID);
		when(gangRepository.load(GROUP_ID, GANG_ID)).thenReturn(gang);
		when(gang.getRules()).thenReturn(rules);
		when(gang.getFighters()).thenReturn(Collections.singleton(fighter));
		when(fighter.getId()).thenReturn(FIGHTER_ID);
		when(fighterRepository.load(GROUP_ID, FIGHTER_ID)).thenReturn(fighter);
		when(fighter.getVersion()).thenReturn(FIGHTER_VERSION);
		when(fighter.getName()).thenReturn(FIGHTER_NAME);
		when(anotherFighter.getId()).thenReturn(ANOTHER_FIGHTER_ID);
		when(anotherFighter.getName()).thenReturn("");
		when(anotherGang.getId()).thenReturn(ANOTHER_GANG_ID);
		when(anotherGang.getFighters()).thenReturn(Collections.singleton(anotherFighter));
		final Set<GangEntity> bothGangs = new HashSet<>();
		bothGangs.add(gang);
		bothGangs.add(anotherGang);
		when(faction.getGangs()).thenReturn(bothGangs);
	}

	@Before
	public void setupInstance() {
		instance = new FighterPersister();
		instance.gangRepository = gangRepository;
		instance.fighterRepository = fighterRepository;
	}
}

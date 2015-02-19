package com.gagror.service.wh40kskirmish.gangs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.gagror.AddError;
import com.gagror.EchoAnswer;
import com.gagror.data.wh40kskirmish.gangs.FighterEntity;
import com.gagror.data.wh40kskirmish.gangs.FighterRecruitInput;
import com.gagror.data.wh40kskirmish.gangs.FighterRepository;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;

@RunWith(MockitoJUnitRunner.class)
public class RecruitFighterPersisterUnitTest {

	private static final Long GROUP_ID = 1L;
	private static final Long GANG_ID = 2L;
	private static final Long ANOTHER_GANG_ID = 3L;
	private static final Long FIGHTER_TYPE_ID = 4L;
	private static final Long GANG_TYPE_ID = 5L;
	private static final String FIGHTER_NAME = "New fighter";
	private static final int GANG_MONEY = 1000;
	private static final int FIGHTER_TYPE_COST = 50;

	RecruitFighterPersister instance;

	@Mock
	GangRepository gangRepository;

	@Mock
	FighterTypeRepository fighterTypeRepository;

	@Mock
	FighterRepository fighterRepository;

	@Mock
	FighterRecruitInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	FactionEntity faction;

	@Mock
	GangEntity gang;

	@Mock
	GangTypeEntity gangType;

	@Mock
	FighterTypeEntity fighterType;

	@Mock
	GangEntity anotherGang;

	@Mock
	FighterEntity anotherGangsFighter;

	@Test
	public void save_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		final ArgumentCaptor<FighterEntity> savedFighter = ArgumentCaptor.forClass(FighterEntity.class);
		verify(fighterRepository).persist(savedFighter.capture());
		assertEquals("Wrong name", FIGHTER_NAME, savedFighter.getValue().getName());
		verify(gang).setMoney(GANG_MONEY-FIGHTER_TYPE_COST);
	}

	@Test
	public void save_bindingErrors() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterRepository, never()).persist(any(FighterEntity.class));
	}

	@Test
	public void save_justEnoughMoney() {
		when(gang.getMoney()).thenReturn(FIGHTER_TYPE_COST);
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(form, never()).addErrorNotEnoughMoney(bindingResult, FIGHTER_TYPE_COST);
	}

	@Test
	public void save_notEnoughMoney() {
		when(gang.getMoney()).thenReturn(FIGHTER_TYPE_COST-1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorNotEnoughMoney(bindingResult, FIGHTER_TYPE_COST);
	}

	@Test
	public void save_fighterTypeNotAvailable() {
		when(fighterType.availableFor(gangType)).thenReturn(false);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorFighterTypeNotAvailable(bindingResult);
	}

	@Test
	public void save_nameNotUnique() {
		when(anotherGangsFighter.getName()).thenReturn(FIGHTER_NAME);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Before
	public void setup() {
		// Set up repositories
		when(gangRepository.load(GROUP_ID, GANG_ID)).thenReturn(gang);
		when(fighterTypeRepository.load(GROUP_ID, FIGHTER_TYPE_ID)).thenReturn(fighterType);
		when(fighterRepository.persist(any(FighterEntity.class))).thenAnswer(new EchoAnswer<>());
		// Set up form
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getGangId()).thenReturn(GANG_ID);
		when(form.getFighterTypeId()).thenReturn(FIGHTER_TYPE_ID);
		when(form.getName()).thenReturn(FIGHTER_NAME);
		AddError.to(bindingResult).when(form).addErrorNotEnoughMoney(bindingResult, FIGHTER_TYPE_COST);
		AddError.to(bindingResult).when(form).addErrorFighterTypeNotAvailable(bindingResult);
		AddError.to(bindingResult).when(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
		// Set up context
		when(gang.getId()).thenReturn(GANG_ID);
		when(anotherGang.getId()).thenReturn(ANOTHER_GANG_ID);
		when(gang.getMoney()).thenReturn(GANG_MONEY);
		when(gangType.getId()).thenReturn(GANG_TYPE_ID);
		when(gang.getGangType()).thenReturn(gangType);
		when(gang.getRules()).thenReturn(rules);
		when(fighterType.getCost()).thenReturn(FIGHTER_TYPE_COST);
		when(fighterType.availableFor(gangType)).thenReturn(true);
		when(rules.getGangTypes()).thenReturn(Collections.singleton(gangType));
		when(gangType.getFactions()).thenReturn(Collections.singleton(faction));
		final Set<GangEntity> bothGangs = new HashSet<>();
		bothGangs.add(gang);
		bothGangs.add(anotherGang);
		when(faction.getGangs()).thenReturn(bothGangs);
		when(anotherGang.getFighters()).thenReturn(Collections.singleton(anotherGangsFighter));
	}

	@Before
	public void setupInstance() {
		instance = new RecruitFighterPersister();
		instance.gangRepository = gangRepository;
		instance.fighterTypeRepository = fighterTypeRepository;
		instance.fighterRepository = fighterRepository;
	}
}

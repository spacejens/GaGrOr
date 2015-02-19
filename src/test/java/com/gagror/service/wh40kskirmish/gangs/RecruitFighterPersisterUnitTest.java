package com.gagror.service.wh40kskirmish.gangs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.gagror.EchoAnswer;
import com.gagror.data.wh40kskirmish.gangs.FighterEntity;
import com.gagror.data.wh40kskirmish.gangs.FighterRecruitInput;
import com.gagror.data.wh40kskirmish.gangs.FighterRepository;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeRepository;

@RunWith(MockitoJUnitRunner.class)
public class RecruitFighterPersisterUnitTest {

	private static final Long GROUP_ID = 1L;
	private static final Long GANG_ID = 2L;
	private static final Long FIGHTER_TYPE_ID = 3L;
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
	GangEntity gang;

	@Mock
	FighterTypeEntity fighterType;

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
		// Set up context
		when(gang.getMoney()).thenReturn(GANG_MONEY);
		when(fighterType.getCost()).thenReturn(FIGHTER_TYPE_COST);
	}

	@Before
	public void setupInstance() {
		instance = new RecruitFighterPersister();
		instance.gangRepository = gangRepository;
		instance.fighterTypeRepository = fighterTypeRepository;
		instance.fighterRepository = fighterRepository;
	}
}

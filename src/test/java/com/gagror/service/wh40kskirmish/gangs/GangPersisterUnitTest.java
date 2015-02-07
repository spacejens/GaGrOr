package com.gagror.service.wh40kskirmish.gangs;

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
import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangInput;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;

@RunWith(MockitoJUnitRunner.class)
public class GangPersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long GANG_TYPE_ID = 5789L;
	private static final Long FACTION_ID = 653L;
	private static final Long PLAYER_ID = 3333L;
	private static final String FORM_GANG_NAME = "Gang form";
	private static final int FORM_MONEY = 112233;
	private static final Long DB_GANG_ID = 11L;
	private static final String DB_GANG_NAME = "Gang DB";
	private static final Long DB_GANG_VERSION = 5L;

	GangPersister instance;

	@Mock
	GangInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	AccountRepository accountRepository;

	@Mock
	GangRepository gangRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	GangTypeEntity gangType;

	@Mock
	FactionEntity faction;

	@Mock
	GangEntity gang;

	@Mock
	AccountEntity player;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		final ArgumentCaptor<GangEntity> savedGang = ArgumentCaptor.forClass(GangEntity.class);
		verify(gangRepository).save(savedGang.capture());
		assertEquals("Wrong name", FORM_GANG_NAME, savedGang.getValue().getName());
		assertEquals("Wrong money", FORM_MONEY, savedGang.getValue().getMoney());
		assertTrue("Not added to faction", faction.getGangs().contains(savedGang.getValue()));
	}

	@Test
	public void save_new_nameNotUnique() {
		whenAnotherGangWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(gangRepository, never()).save(any(GangEntity.class));
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(gangRepository, never()).save(any(GangEntity.class));
	}

	@Test(expected=DataNotFoundException.class)
	public void save_new_playerNotFound() {
		doThrow(DataNotFoundException.class).when(accountRepository).load(PLAYER_ID);
		instance.save(form, bindingResult);
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
	public void save_new_factionNotFound() {
		gangType.getFactions().remove(faction);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenGangExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		verify(gangRepository, never()).save(any(GangEntity.class));
		verify(gang).setName(FORM_GANG_NAME);
		verify(gang).setMoney(FORM_MONEY);
	}

	@Test
	public void save_existing_nameNotUnique() {
		whenGangExists();
		whenAnotherGangWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(gang, never()).setName(anyString());
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_existing_bindingError() {
		whenGangExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(gang, never()).setName(anyString());
		verify(gang, never()).setMoney(anyInt());
		verify(gangRepository, never()).save(any(GangEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_existing_wrongGroupType() {
		whenGangExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_gangTypeNotFound() {
		whenGangExists();
		rules.getGangTypes().remove(gangType);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_factionNotFound() {
		whenGangExists();
		gangType.getFactions().remove(faction);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_notFoundInGangType() {
		whenGangExists();
		faction.getGangs().remove(gang);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenGangExists();
		when(form.getVersion()).thenReturn(DB_GANG_VERSION - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(gang, never()).setName(anyString());
		verify(gang, never()).setMoney(anyInt());
	}

	protected void whenAnotherGangWithSameNameExists() {
		final GangTypeEntity anotherGangType = mock(GangTypeEntity.class);
		when(anotherGangType.getId()).thenReturn(GANG_TYPE_ID + 1);
		when(anotherGangType.getFactions()).thenReturn(new HashSet<FactionEntity>());
		rules.getGangTypes().add(anotherGangType);
		final FactionEntity anotherFaction = mock(FactionEntity.class);
		when(anotherFaction.getId()).thenReturn(FACTION_ID + 1);
		when(anotherFaction.getGangs()).thenReturn(new HashSet<GangEntity>());
		anotherGangType.getFactions().add(anotherFaction);
		final GangEntity anotherGang = mock(GangEntity.class);
		when(anotherGang.getId()).thenReturn(DB_GANG_ID + 1);
		when(anotherGang.getName()).thenReturn(FORM_GANG_NAME);
		anotherFaction.getGangs().add(anotherGang);
	}

	protected void whenGangExists() {
		when(form.getId()).thenReturn(DB_GANG_ID);
		when(form.getVersion()).thenReturn(DB_GANG_VERSION);
		when(gang.getId()).thenReturn(DB_GANG_ID);
		when(gang.getVersion()).thenReturn(DB_GANG_VERSION);
		when(gang.getName()).thenReturn(DB_GANG_NAME);
		when(gang.getFaction()).thenReturn(faction);
		faction.getGangs().add(gang);
		// Player ID in form is only considered when creating new gang, so we null it to detect any dependency
		when(form.getPlayerId()).thenReturn(null);
	}

	@Before
	public void setupForm() {
		when(form.getId()).thenReturn(null);
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getFactionId()).thenReturn(FACTION_ID);
		when(form.getPlayerId()).thenReturn(PLAYER_ID);
		when(form.getName()).thenReturn(FORM_GANG_NAME);
		when(form.getMoney()).thenReturn(FORM_MONEY);
		AddError.to(bindingResult).when(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
		AddError.to(bindingResult).when(form).addErrorSimultaneuosEdit(bindingResult);
	}

	@Before
	public void setupContext() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.load(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getGroup()).thenReturn(group);
		when(rules.getGangTypes()).thenReturn(new HashSet<GangTypeEntity>());
		when(gangType.getId()).thenReturn(GANG_TYPE_ID);
		when(gangType.getFactions()).thenReturn(new HashSet<FactionEntity>());
		when(gangType.getRules()).thenReturn(rules);
		rules.getGangTypes().add(gangType);
		when(faction.getId()).thenReturn(FACTION_ID);
		when(faction.getGangs()).thenReturn(new HashSet<GangEntity>());
		when(faction.getGangType()).thenReturn(gangType);
		gangType.getFactions().add(faction);
		when(player.getId()).thenReturn(PLAYER_ID);
		when(accountRepository.load(PLAYER_ID)).thenReturn(player);
	}

	@Before
	public void setupInstance() {
		instance = new GangPersister();
		instance.groupRepository = groupRepository;
		instance.accountRepository = accountRepository;
		instance.gangRepository = gangRepository;
	}
}

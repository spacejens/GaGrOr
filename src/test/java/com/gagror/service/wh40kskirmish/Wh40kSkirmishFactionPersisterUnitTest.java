package com.gagror.service.wh40kskirmish;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
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

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishFactionEntity;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishFactionInput;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishFactionRepository;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishRulesEntity;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishFactionPersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long GANG_TYPE_ID = 5789L;
	private static final String FORM_FACTION_NAME = "Faction form";

	Wh40kSkirmishFactionPersister instance;

	@Mock
	Wh40kSkirmishFactionInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	Wh40kSkirmishFactionRepository factionRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	Wh40kSkirmishGangTypeEntity gangType;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<Wh40kSkirmishFactionEntity> savedFaction = ArgumentCaptor.forClass(Wh40kSkirmishFactionEntity.class);
		verify(factionRepository).save(savedFaction.capture());
		assertEquals("Wrong name", FORM_FACTION_NAME, savedFaction.getValue().getName());
		assertTrue("Not added to gang type", gangType.getFactions().contains(savedFaction.getValue()));
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(factionRepository, never()).save(any(Wh40kSkirmishFactionEntity.class));
	}

	@Test(expected=IllegalStateException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=IllegalStateException.class)
	public void save_new_gangTypeNotFound() {
		rules.getGangTypes().remove(gangType);
		instance.save(form, bindingResult);
	}

	@Before
	public void setupForm() {
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getGangTypeId()).thenReturn(GANG_TYPE_ID);
		when(form.getName()).thenReturn(FORM_FACTION_NAME);
	}

	@Before
	public void setupContext() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getGangTypes()).thenReturn(new HashSet<Wh40kSkirmishGangTypeEntity>());
		when(gangType.getId()).thenReturn(GANG_TYPE_ID);
		when(gangType.getFactions()).thenReturn(new HashSet<Wh40kSkirmishFactionEntity>());
		when(gangType.getRules()).thenReturn(rules);
		rules.getGangTypes().add(gangType);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishFactionPersister();
		instance.groupRepository = groupRepository;
		instance.factionRepository = factionRepository;
	}
}

package com.gagror.service.wh40kskirmish;

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
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeRepository;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishGangTypePersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final String FORM_GANG_TYPE_NAME = "Gang type form";
	private static final Long DB_GANG_TYPE_ID = 5678L;
	private static final String DB_GANG_TYPE_NAME = "Gang type DB";
	private static final Long DB_GANG_TYPE_VERSION = 5L;

	Wh40kSkirmishGangTypePersister instance;

	@Mock
	Wh40kSkirmishGangTypeInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	Wh40kSkirmishGangTypeRepository gangTypeRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishGangTypeEntity gangType;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<Wh40kSkirmishGangTypeEntity> savedGangType = ArgumentCaptor.forClass(Wh40kSkirmishGangTypeEntity.class);
		verify(gangTypeRepository).save(savedGangType.capture());
		assertEquals("Wrong name", FORM_GANG_TYPE_NAME, savedGangType.getValue().getName());
		assertTrue("Not added to rules", rules.getGangTypes().contains(savedGangType.getValue()));
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(gangTypeRepository, never()).save(any(Wh40kSkirmishGangTypeEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenGangTypeExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		verify(gangTypeRepository, never()).save(any(Wh40kSkirmishGangTypeEntity.class));
		verify(gangType).setName(FORM_GANG_TYPE_NAME);
	}

	@Test
	public void save_existing_bindingError() {
		whenGangTypeExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(gangType, never()).setName(anyString());
		verify(gangTypeRepository, never()).save(any(Wh40kSkirmishGangTypeEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_existing_wrongGroupType() {
		whenGangTypeExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_notFoundInGroup() {
		whenGangTypeExists();
		rules.getGangTypes().remove(gangType);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenGangTypeExists();
		when(form.getVersion()).thenReturn(DB_GANG_TYPE_VERSION - 1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(gangType, never()).setName(anyString());
	}

	protected void whenGangTypeExists() {
		when(form.getId()).thenReturn(DB_GANG_TYPE_ID);
		when(form.getVersion()).thenReturn(DB_GANG_TYPE_VERSION);
		when(gangType.getId()).thenReturn(DB_GANG_TYPE_ID);
		when(gangType.getVersion()).thenReturn(DB_GANG_TYPE_VERSION);
		when(gangType.getName()).thenReturn(DB_GANG_TYPE_NAME);
		when(gangType.getRules()).thenReturn(rules);
		rules.getGangTypes().add(gangType);
	}

	@Before
	public void setupForm() {
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getName()).thenReturn(FORM_GANG_TYPE_NAME);
	}

	@Before
	public void setupGroup() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getGangTypes()).thenReturn(new HashSet<Wh40kSkirmishGangTypeEntity>());
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishGangTypePersister();
		instance.groupRepository = groupRepository;
		instance.gangTypeRepository = gangTypeRepository;
	}
}

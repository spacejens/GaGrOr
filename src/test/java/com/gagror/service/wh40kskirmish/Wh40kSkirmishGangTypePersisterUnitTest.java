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
import com.gagror.data.wh40kskirmish.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishGangTypeInput;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishGangTypeRepository;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishRulesEntity;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishGangTypePersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final String GANG_TYPE_NAME = "Gang type name";

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
	Wh40kSkirmishRulesEntity rules;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<Wh40kSkirmishGangTypeEntity> savedGangType = ArgumentCaptor.forClass(Wh40kSkirmishGangTypeEntity.class);
		verify(gangTypeRepository).save(savedGangType.capture());
		assertEquals("Wrong name", GANG_TYPE_NAME, savedGangType.getValue().getName());
		assertTrue("Not added to rules", rules.getGangTypes().contains(savedGangType.getValue()));
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(gangTypeRepository, never()).save(any(Wh40kSkirmishGangTypeEntity.class));
	}

	@Test(expected=IllegalStateException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	// TODO Add unit tests for editing existing gang type

	@Before
	public void setupForm() {
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getName()).thenReturn(GANG_TYPE_NAME);
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

package com.gagror.service.wh40kskirmish;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesInput;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishRulesPersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long RULES_ID = 5678L;
	private static final Long DB_RULES_VERSION = 5L;

	Wh40kSkirmishRulesPersister instance;

	@Mock
	Wh40kSkirmishRulesInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Test
	public void save_existing_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		// TODO Verify that rules entity is edited
	}

	@Test
	public void save_existing_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		// TODO Verify that rules entity is never edited
	}

	@Test(expected=IllegalStateException.class)
	public void save_existing_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		when(form.getVersion()).thenReturn(DB_RULES_VERSION - 1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		// TODO Verify that rules entity is never edited
	}

	@Before
	public void setupForm() {
		when(form.getId()).thenReturn(RULES_ID);
		when(form.getVersion()).thenReturn(DB_RULES_VERSION);
		when(form.getGroupId()).thenReturn(GROUP_ID);
	}

	@Before
	public void setupGroup() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getId()).thenReturn(RULES_ID);
		when(rules.getVersion()).thenReturn(DB_RULES_VERSION);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishRulesPersister();
		instance.groupRepository = groupRepository;
	}
}

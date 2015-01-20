package com.gagror.service.wh40kskirmish;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
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
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesInput;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishRulesPersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long RULES_ID = 5678L;
	private static final Long DB_RULES_VERSION = 5L;
	private static final int FORM_STARTING_MONEY = 1000;
	private static final String FORM_CURRENCY_NAME = "Currency";

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
		// Verify that the entity is updated
		verify(rules).setStartingMoney(FORM_STARTING_MONEY);
		verify(rules).setCurrencyName(FORM_CURRENCY_NAME);
	}

	@Test
	public void save_existing_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verifyEntityNotUpdated();
	}

	private void verifyEntityNotUpdated() {
		verify(rules, never()).setStartingMoney(anyInt());
		verify(rules, never()).setCurrencyName(anyString());
	}

	@Test(expected=WrongGroupTypeException.class)
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
		verifyEntityNotUpdated();
	}

	@Before
	public void setupForm() {
		when(form.getId()).thenReturn(RULES_ID);
		when(form.getVersion()).thenReturn(DB_RULES_VERSION);
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getStartingMoney()).thenReturn(FORM_STARTING_MONEY);
		when(form.getCurrencyName()).thenReturn(FORM_CURRENCY_NAME);
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

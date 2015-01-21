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
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryInput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryRepository;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishTerritoryCategoryPersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final String FORM_TERRITORY_CATEGORY_NAME = "Territory category form";
	private static final Long DB_TERRITORY_CATEGORY_ID = 5678L;
	private static final String DB_TERRITORY_CATEGORY_NAME = "Territory category DB";
	private static final Long DB_TERRITORY_CATEGORY_VERSION = 5L;

	Wh40kSkirmishTerritoryCategoryPersister instance;

	@Mock
	Wh40kSkirmishTerritoryCategoryInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	Wh40kSkirmishTerritoryCategoryRepository territoryCategoryRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishTerritoryCategoryEntity territoryCategory;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<Wh40kSkirmishTerritoryCategoryEntity> savedTerritoryCategory = ArgumentCaptor.forClass(Wh40kSkirmishTerritoryCategoryEntity.class);
		verify(territoryCategoryRepository).save(savedTerritoryCategory.capture());
		assertEquals("Wrong name", FORM_TERRITORY_CATEGORY_NAME, savedTerritoryCategory.getValue().getName());
		assertTrue("Not added to rules", rules.getTerritoryCategories().contains(savedTerritoryCategory.getValue()));
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(territoryCategoryRepository, never()).save(any(Wh40kSkirmishTerritoryCategoryEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenTerritoryCategoryExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		verify(territoryCategoryRepository, never()).save(any(Wh40kSkirmishTerritoryCategoryEntity.class));
		verify(territoryCategory).setName(FORM_TERRITORY_CATEGORY_NAME);
	}

	@Test
	public void save_existing_bindingError() {
		whenTerritoryCategoryExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(territoryCategory, never()).setName(anyString());
		verify(territoryCategoryRepository, never()).save(any(Wh40kSkirmishTerritoryCategoryEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_existing_wrongGroupType() {
		whenTerritoryCategoryExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_notFoundInGroup() {
		whenTerritoryCategoryExists();
		rules.getTerritoryCategories().remove(territoryCategory);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenTerritoryCategoryExists();
		when(form.getVersion()).thenReturn(DB_TERRITORY_CATEGORY_VERSION - 1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(territoryCategory, never()).setName(anyString());
	}

	protected void whenTerritoryCategoryExists() {
		when(form.getId()).thenReturn(DB_TERRITORY_CATEGORY_ID);
		when(form.getVersion()).thenReturn(DB_TERRITORY_CATEGORY_VERSION);
		when(territoryCategory.getId()).thenReturn(DB_TERRITORY_CATEGORY_ID);
		when(territoryCategory.getVersion()).thenReturn(DB_TERRITORY_CATEGORY_VERSION);
		when(territoryCategory.getName()).thenReturn(DB_TERRITORY_CATEGORY_NAME);
		when(territoryCategory.getRules()).thenReturn(rules);
		rules.getTerritoryCategories().add(territoryCategory);
	}

	@Before
	public void setupForm() {
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getName()).thenReturn(FORM_TERRITORY_CATEGORY_NAME);
	}

	@Before
	public void setupGroup() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getTerritoryCategories()).thenReturn(new HashSet<Wh40kSkirmishTerritoryCategoryEntity>());
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishTerritoryCategoryPersister();
		instance.groupRepository = groupRepository;
		instance.territoryCategoryRepository = territoryCategoryRepository;
	}
}

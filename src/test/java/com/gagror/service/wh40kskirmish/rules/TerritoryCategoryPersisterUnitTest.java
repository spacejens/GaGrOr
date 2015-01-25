package com.gagror.service.wh40kskirmish.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryInput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryRepository;

@RunWith(MockitoJUnitRunner.class)
public class TerritoryCategoryPersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final String FORM_TERRITORY_CATEGORY_NAME = "Territory category form";
	private static final Long DB_TERRITORY_CATEGORY_ID = 5678L;
	private static final String DB_TERRITORY_CATEGORY_NAME = "Territory category DB";
	private static final Long DB_TERRITORY_CATEGORY_VERSION = 5L;

	TerritoryCategoryPersister instance;

	@Mock
	TerritoryCategoryInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	TerritoryCategoryRepository territoryCategoryRepository;

	@Mock
	GroupEntity group;

	@Mock
	TerritoryCategoryEntity territoryCategory;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		final ArgumentCaptor<TerritoryCategoryEntity> savedTerritoryCategory = ArgumentCaptor.forClass(TerritoryCategoryEntity.class);
		verify(territoryCategoryRepository).save(savedTerritoryCategory.capture());
		assertEquals("Wrong name", FORM_TERRITORY_CATEGORY_NAME, savedTerritoryCategory.getValue().getName());
		assertTrue("Not added to rules", rules.getTerritoryCategories().contains(savedTerritoryCategory.getValue()));
	}

	@Test
	public void save_new_nameNotUnique() {
		whenAnotherCategoryWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(territoryCategoryRepository, never()).save(any(TerritoryCategoryEntity.class));
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(territoryCategoryRepository, never()).save(any(TerritoryCategoryEntity.class));
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
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		verify(territoryCategoryRepository, never()).save(any(TerritoryCategoryEntity.class));
		verify(territoryCategory).setName(FORM_TERRITORY_CATEGORY_NAME);
	}

	@Test
	public void save_existing_nameNotUnique() {
		whenTerritoryCategoryExists();
		whenAnotherCategoryWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(territoryCategory, never()).setName(anyString());
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_existing_bindingError() {
		whenTerritoryCategoryExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(territoryCategory, never()).setName(anyString());
		verify(territoryCategoryRepository, never()).save(any(TerritoryCategoryEntity.class));
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
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(territoryCategory, never()).setName(anyString());
	}

	protected void whenAnotherCategoryWithSameNameExists() {
		final TerritoryCategoryEntity anotherCategory = mock(TerritoryCategoryEntity.class);
		when(anotherCategory.getId()).thenReturn(DB_TERRITORY_CATEGORY_ID + 1);
		when(anotherCategory.getName()).thenReturn(FORM_TERRITORY_CATEGORY_NAME);
		rules.getTerritoryCategories().add(anotherCategory);
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
		AddError.to(bindingResult).when(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
		AddError.to(bindingResult).when(form).addErrorSimultaneuosEdit(bindingResult);
	}

	@Before
	public void setupGroup() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getTerritoryCategories()).thenReturn(new HashSet<TerritoryCategoryEntity>());
	}

	@Before
	public void setupInstance() {
		instance = new TerritoryCategoryPersister();
		instance.groupRepository = groupRepository;
		instance.territoryCategoryRepository = territoryCategoryRepository;
	}
}

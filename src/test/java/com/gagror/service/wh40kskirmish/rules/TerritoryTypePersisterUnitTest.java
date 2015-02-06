package com.gagror.service.wh40kskirmish.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
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

import com.gagror.AddError;
import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeInput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeRepository;

@RunWith(MockitoJUnitRunner.class)
public class TerritoryTypePersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long TERRITORY_CATEGORY_ID = 5789L;
	private static final String FORM_TERRITORY_TYPE_NAME = "Territory type form";
	private static final Long DB_TERRITORY_TYPE_ID = 11L;
	private static final String DB_TERRITORY_TYPE_NAME = "Territory type DB";
	private static final Long DB_TERRITORY_TYPE_VERSION = 5L;

	TerritoryTypePersister instance;

	@Mock
	TerritoryTypeInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	TerritoryTypeRepository territoryTypeRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	TerritoryCategoryEntity territoryCategory;

	@Mock
	TerritoryTypeEntity territoryType;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		final ArgumentCaptor<TerritoryTypeEntity> savedTerritoryType = ArgumentCaptor.forClass(TerritoryTypeEntity.class);
		verify(territoryTypeRepository).save(savedTerritoryType.capture());
		assertEquals("Wrong name", FORM_TERRITORY_TYPE_NAME, savedTerritoryType.getValue().getName());
		assertTrue("Not added to territory category", territoryCategory.getTerritoryTypes().contains(savedTerritoryType.getValue()));
	}

	@Test
	public void save_new_nameNotUnique() {
		whenAnotherTerritoryTypeWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(territoryTypeRepository, never()).save(any(TerritoryTypeEntity.class));
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(territoryTypeRepository, never()).save(any(TerritoryTypeEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_new_territoryCategoryNotFound() {
		rules.getTerritoryCategories().remove(territoryCategory);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenTerritoryTypeExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		verify(territoryTypeRepository, never()).save(any(TerritoryTypeEntity.class));
		verify(territoryType).setName(FORM_TERRITORY_TYPE_NAME);
	}

	@Test
	public void save_existing_nameNotUnique() {
		whenTerritoryTypeExists();
		whenAnotherTerritoryTypeWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(territoryType, never()).setName(anyString());
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_existing_bindingError() {
		whenTerritoryTypeExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(territoryType, never()).setName(anyString());
		verify(territoryTypeRepository, never()).save(any(TerritoryTypeEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_existing_wrongGroupType() {
		whenTerritoryTypeExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_territoryCategoryNotFound() {
		whenTerritoryTypeExists();
		rules.getTerritoryCategories().remove(territoryCategory);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_notFoundInTerritoryCategory() {
		whenTerritoryTypeExists();
		territoryCategory.getTerritoryTypes().remove(territoryType);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenTerritoryTypeExists();
		when(form.getVersion()).thenReturn(DB_TERRITORY_TYPE_VERSION - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(territoryType, never()).setName(anyString());
	}

	protected void whenAnotherTerritoryTypeWithSameNameExists() {
		final TerritoryCategoryEntity anotherCategory = mock(TerritoryCategoryEntity.class);
		when(anotherCategory.getId()).thenReturn(TERRITORY_CATEGORY_ID + 1);
		when(anotherCategory.getTerritoryTypes()).thenReturn(new HashSet<TerritoryTypeEntity>());
		rules.getTerritoryCategories().add(anotherCategory);
		final TerritoryTypeEntity anotherTerritoryType = mock(TerritoryTypeEntity.class);
		when(anotherTerritoryType.getId()).thenReturn(DB_TERRITORY_TYPE_ID + 1);
		when(anotherTerritoryType.getName()).thenReturn(FORM_TERRITORY_TYPE_NAME);
		anotherCategory.getTerritoryTypes().add(anotherTerritoryType);
	}

	protected void whenTerritoryTypeExists() {
		when(form.getId()).thenReturn(DB_TERRITORY_TYPE_ID);
		when(form.getVersion()).thenReturn(DB_TERRITORY_TYPE_VERSION);
		when(territoryType.getId()).thenReturn(DB_TERRITORY_TYPE_ID);
		when(territoryType.getVersion()).thenReturn(DB_TERRITORY_TYPE_VERSION);
		when(territoryType.getName()).thenReturn(DB_TERRITORY_TYPE_NAME);
		when(territoryType.getTerritoryCategory()).thenReturn(territoryCategory);
		territoryCategory.getTerritoryTypes().add(territoryType);
	}

	@Before
	public void setupForm() {
		when(form.getId()).thenReturn(null);
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getTerritoryCategoryId()).thenReturn(TERRITORY_CATEGORY_ID);
		when(form.getName()).thenReturn(FORM_TERRITORY_TYPE_NAME);
		AddError.to(bindingResult).when(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
		AddError.to(bindingResult).when(form).addErrorSimultaneuosEdit(bindingResult);
	}

	@Before
	public void setupContext() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.load(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getTerritoryCategories()).thenReturn(new HashSet<TerritoryCategoryEntity>());
		when(territoryCategory.getId()).thenReturn(TERRITORY_CATEGORY_ID);
		when(territoryCategory.getTerritoryTypes()).thenReturn(new HashSet<TerritoryTypeEntity>());
		when(territoryCategory.getRules()).thenReturn(rules);
		rules.getTerritoryCategories().add(territoryCategory);
	}

	@Before
	public void setupInstance() {
		instance = new TerritoryTypePersister();
		instance.groupRepository = groupRepository;
		instance.territoryTypeRepository = territoryTypeRepository;
	}
}

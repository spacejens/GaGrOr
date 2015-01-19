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

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryTypeEntity;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryTypeInput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryTypeRepository;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishTerritoryTypePersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long TERRITORY_CATEGORY_ID = 5789L;
	private static final String FORM_TERRITORY_TYPE_NAME = "Territory type form";
	private static final Long DB_TERRITORY_TYPE_ID = 11L;
	private static final String DB_TERRITORY_TYPE_NAME = "Territory type DB";
	private static final Long DB_TERRITORY_TYPE_VERSION = 5L;

	Wh40kSkirmishTerritoryTypePersister instance;

	@Mock
	Wh40kSkirmishTerritoryTypeInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	Wh40kSkirmishTerritoryTypeRepository territoryTypeRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	Wh40kSkirmishTerritoryCategoryEntity territoryCategory;

	@Mock
	Wh40kSkirmishTerritoryTypeEntity territoryType;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<Wh40kSkirmishTerritoryTypeEntity> savedTerritoryType = ArgumentCaptor.forClass(Wh40kSkirmishTerritoryTypeEntity.class);
		verify(territoryTypeRepository).save(savedTerritoryType.capture());
		assertEquals("Wrong name", FORM_TERRITORY_TYPE_NAME, savedTerritoryType.getValue().getName());
		assertTrue("Not added to territory category", territoryCategory.getTerritoryTypes().contains(savedTerritoryType.getValue()));
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(territoryTypeRepository, never()).save(any(Wh40kSkirmishTerritoryTypeEntity.class));
	}

	@Test(expected=IllegalStateException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=IllegalStateException.class)
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
		verify(territoryTypeRepository, never()).save(any(Wh40kSkirmishTerritoryTypeEntity.class));
		verify(territoryType).setName(FORM_TERRITORY_TYPE_NAME);
	}

	@Test
	public void save_existing_bindingError() {
		whenTerritoryTypeExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(territoryType, never()).setName(anyString());
		verify(territoryTypeRepository, never()).save(any(Wh40kSkirmishTerritoryTypeEntity.class));
	}

	@Test(expected=IllegalStateException.class)
	public void save_existing_wrongGroupType() {
		whenTerritoryTypeExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=IllegalStateException.class)
	public void save_existing_territoryCategoryNotFound() {
		whenTerritoryTypeExists();
		rules.getTerritoryCategories().remove(territoryCategory);
		instance.save(form, bindingResult);
	}

	@Test(expected=IllegalStateException.class)
	public void save_existing_notFoundInTerritoryCategory() {
		whenTerritoryTypeExists();
		territoryCategory.getTerritoryTypes().remove(territoryType);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenTerritoryTypeExists();
		when(form.getVersion()).thenReturn(DB_TERRITORY_TYPE_VERSION - 1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(territoryType, never()).setName(anyString());
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
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getTerritoryCategoryId()).thenReturn(TERRITORY_CATEGORY_ID);
		when(form.getName()).thenReturn(FORM_TERRITORY_TYPE_NAME);
	}

	@Before
	public void setupContext() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getTerritoryCategories()).thenReturn(new HashSet<Wh40kSkirmishTerritoryCategoryEntity>());
		when(territoryCategory.getId()).thenReturn(TERRITORY_CATEGORY_ID);
		when(territoryCategory.getTerritoryTypes()).thenReturn(new HashSet<Wh40kSkirmishTerritoryTypeEntity>());
		when(territoryCategory.getRules()).thenReturn(rules);
		rules.getTerritoryCategories().add(territoryCategory);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishTerritoryTypePersister();
		instance.groupRepository = groupRepository;
		instance.territoryTypeRepository = territoryTypeRepository;
	}
}

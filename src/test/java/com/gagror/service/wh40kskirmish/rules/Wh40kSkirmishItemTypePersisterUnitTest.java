package com.gagror.service.wh40kskirmish.rules;

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
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemTypeEntity;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemTypeInput;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemTypeRepository;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishItemTypePersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long ITEM_CATEGORY_ID = 5789L;
	private static final String FORM_ITEM_TYPE_NAME = "Item type form";
	private static final Long DB_ITEM_TYPE_ID = 11L;
	private static final String DB_ITEM_TYPE_NAME = "Item type DB";
	private static final Long DB_ITEM_TYPE_VERSION = 5L;

	Wh40kSkirmishItemTypePersister instance;

	@Mock
	Wh40kSkirmishItemTypeInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	Wh40kSkirmishItemTypeRepository itemTypeRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	Wh40kSkirmishItemCategoryEntity itemCategory;

	@Mock
	Wh40kSkirmishItemTypeEntity itemType;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<Wh40kSkirmishItemTypeEntity> savedItemType = ArgumentCaptor.forClass(Wh40kSkirmishItemTypeEntity.class);
		verify(itemTypeRepository).save(savedItemType.capture());
		assertEquals("Wrong name", FORM_ITEM_TYPE_NAME, savedItemType.getValue().getName());
		assertTrue("Not added to item category", itemCategory.getItemTypes().contains(savedItemType.getValue()));
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(itemTypeRepository, never()).save(any(Wh40kSkirmishItemTypeEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_new_itemCategoryNotFound() {
		rules.getItemCategories().remove(itemCategory);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenItemTypeExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		verify(itemTypeRepository, never()).save(any(Wh40kSkirmishItemTypeEntity.class));
		verify(itemType).setName(FORM_ITEM_TYPE_NAME);
	}

	@Test
	public void save_existing_bindingError() {
		whenItemTypeExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(itemType, never()).setName(anyString());
		verify(itemTypeRepository, never()).save(any(Wh40kSkirmishItemTypeEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_existing_wrongGroupType() {
		whenItemTypeExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_itemCategoryNotFound() {
		whenItemTypeExists();
		rules.getItemCategories().remove(itemCategory);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_notFoundInItemCategory() {
		whenItemTypeExists();
		itemCategory.getItemTypes().remove(itemType);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenItemTypeExists();
		when(form.getVersion()).thenReturn(DB_ITEM_TYPE_VERSION - 1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(itemType, never()).setName(anyString());
	}

	protected void whenItemTypeExists() {
		when(form.getId()).thenReturn(DB_ITEM_TYPE_ID);
		when(form.getVersion()).thenReturn(DB_ITEM_TYPE_VERSION);
		when(itemType.getId()).thenReturn(DB_ITEM_TYPE_ID);
		when(itemType.getVersion()).thenReturn(DB_ITEM_TYPE_VERSION);
		when(itemType.getName()).thenReturn(DB_ITEM_TYPE_NAME);
		when(itemType.getItemCategory()).thenReturn(itemCategory);
		itemCategory.getItemTypes().add(itemType);
	}

	@Before
	public void setupForm() {
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getItemCategoryId()).thenReturn(ITEM_CATEGORY_ID);
		when(form.getName()).thenReturn(FORM_ITEM_TYPE_NAME);
	}

	@Before
	public void setupContext() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getItemCategories()).thenReturn(new HashSet<Wh40kSkirmishItemCategoryEntity>());
		when(itemCategory.getId()).thenReturn(ITEM_CATEGORY_ID);
		when(itemCategory.getItemTypes()).thenReturn(new HashSet<Wh40kSkirmishItemTypeEntity>());
		when(itemCategory.getRules()).thenReturn(rules);
		rules.getItemCategories().add(itemCategory);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishItemTypePersister();
		instance.groupRepository = groupRepository;
		instance.itemTypeRepository = itemTypeRepository;
	}
}

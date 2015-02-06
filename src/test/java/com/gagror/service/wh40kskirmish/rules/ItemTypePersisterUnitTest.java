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
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeInput;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeRepository;

@RunWith(MockitoJUnitRunner.class)
public class ItemTypePersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long ITEM_CATEGORY_ID = 5789L;
	private static final String FORM_ITEM_TYPE_NAME = "Item type form";
	private static final Long DB_ITEM_TYPE_ID = 11L;
	private static final String DB_ITEM_TYPE_NAME = "Item type DB";
	private static final Long DB_ITEM_TYPE_VERSION = 5L;

	ItemTypePersister instance;

	@Mock
	ItemTypeInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	ItemTypeRepository itemTypeRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	ItemCategoryEntity itemCategory;

	@Mock
	ItemTypeEntity itemType;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		final ArgumentCaptor<ItemTypeEntity> savedItemType = ArgumentCaptor.forClass(ItemTypeEntity.class);
		verify(itemTypeRepository).save(savedItemType.capture());
		assertEquals("Wrong name", FORM_ITEM_TYPE_NAME, savedItemType.getValue().getName());
		assertTrue("Not added to item category", itemCategory.getItemTypes().contains(savedItemType.getValue()));
	}

	@Test
	public void save_new_nameNotUnique() {
		whenAnotherItemTypeWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(itemTypeRepository, never()).save(any(ItemTypeEntity.class));
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(itemTypeRepository, never()).save(any(ItemTypeEntity.class));
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
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		verify(itemTypeRepository, never()).save(any(ItemTypeEntity.class));
		verify(itemType).setName(FORM_ITEM_TYPE_NAME);
	}

	@Test
	public void save_existing_nameNotUnique() {
		whenItemTypeExists();
		whenAnotherItemTypeWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(itemType, never()).setName(anyString());
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_existing_bindingError() {
		whenItemTypeExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(itemType, never()).setName(anyString());
		verify(itemTypeRepository, never()).save(any(ItemTypeEntity.class));
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
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(itemType, never()).setName(anyString());
	}

	protected void whenAnotherItemTypeWithSameNameExists() {
		final ItemCategoryEntity anotherCategory = mock(ItemCategoryEntity.class);
		when(anotherCategory.getId()).thenReturn(ITEM_CATEGORY_ID + 1);
		when(anotherCategory.getItemTypes()).thenReturn(new HashSet<ItemTypeEntity>());
		rules.getItemCategories().add(anotherCategory);
		final ItemTypeEntity anotherItemType = mock(ItemTypeEntity.class);
		when(anotherItemType.getId()).thenReturn(DB_ITEM_TYPE_ID + 1);
		when(anotherItemType.getName()).thenReturn(FORM_ITEM_TYPE_NAME);
		anotherCategory.getItemTypes().add(anotherItemType);
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
		when(form.getId()).thenReturn(null);
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getItemCategoryId()).thenReturn(ITEM_CATEGORY_ID);
		when(form.getName()).thenReturn(FORM_ITEM_TYPE_NAME);
		AddError.to(bindingResult).when(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
		AddError.to(bindingResult).when(form).addErrorSimultaneuosEdit(bindingResult);
	}

	@Before
	public void setupContext() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.load(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getItemCategories()).thenReturn(new HashSet<ItemCategoryEntity>());
		when(itemCategory.getId()).thenReturn(ITEM_CATEGORY_ID);
		when(itemCategory.getItemTypes()).thenReturn(new HashSet<ItemTypeEntity>());
		when(itemCategory.getRules()).thenReturn(rules);
		rules.getItemCategories().add(itemCategory);
	}

	@Before
	public void setupInstance() {
		instance = new ItemTypePersister();
		instance.groupRepository = groupRepository;
		instance.itemTypeRepository = itemTypeRepository;
	}
}

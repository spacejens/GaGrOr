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
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryInput;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryRepository;

@RunWith(MockitoJUnitRunner.class)
public class ItemCategoryPersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final String FORM_ITEM_CATEGORY_NAME = "Item category form";
	private static final Long DB_ITEM_CATEGORY_ID = 5678L;
	private static final String DB_ITEM_CATEGORY_NAME = "Item category DB";
	private static final Long DB_ITEM_CATEGORY_VERSION = 5L;

	ItemCategoryPersister instance;

	@Mock
	ItemCategoryInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	ItemCategoryRepository itemCategoryRepository;

	@Mock
	GroupEntity group;

	@Mock
	ItemCategoryEntity itemCategory;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		final ArgumentCaptor<ItemCategoryEntity> savedItemCategory = ArgumentCaptor.forClass(ItemCategoryEntity.class);
		verify(itemCategoryRepository).save(savedItemCategory.capture());
		assertEquals("Wrong name", FORM_ITEM_CATEGORY_NAME, savedItemCategory.getValue().getName());
		assertTrue("Not added to rules", rules.getItemCategories().contains(savedItemCategory.getValue()));
	}

	@Test
	public void save_new_nameNotUnique() {
		whenAnotherCategoryWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(itemCategoryRepository, never()).save(any(ItemCategoryEntity.class));
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(itemCategoryRepository, never()).save(any(ItemCategoryEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenItemCategoryExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		verify(itemCategoryRepository, never()).save(any(ItemCategoryEntity.class));
		verify(itemCategory).setName(FORM_ITEM_CATEGORY_NAME);
	}

	@Test
	public void save_existing_nameNotUnique() {
		whenItemCategoryExists();
		whenAnotherCategoryWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(itemCategory, never()).setName(anyString());
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_existing_bindingError() {
		whenItemCategoryExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(itemCategory, never()).setName(anyString());
		verify(itemCategoryRepository, never()).save(any(ItemCategoryEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_existing_wrongGroupType() {
		whenItemCategoryExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_notFoundInGroup() {
		whenItemCategoryExists();
		rules.getItemCategories().remove(itemCategory);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenItemCategoryExists();
		when(form.getVersion()).thenReturn(DB_ITEM_CATEGORY_VERSION - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(itemCategory, never()).setName(anyString());
	}

	protected void whenAnotherCategoryWithSameNameExists() {
		final ItemCategoryEntity anotherCategory = mock(ItemCategoryEntity.class);
		when(anotherCategory.getId()).thenReturn(DB_ITEM_CATEGORY_ID + 1);
		when(anotherCategory.getName()).thenReturn(FORM_ITEM_CATEGORY_NAME);
		rules.getItemCategories().add(anotherCategory);
	}

	protected void whenItemCategoryExists() {
		when(form.getId()).thenReturn(DB_ITEM_CATEGORY_ID);
		when(form.getVersion()).thenReturn(DB_ITEM_CATEGORY_VERSION);
		when(itemCategory.getId()).thenReturn(DB_ITEM_CATEGORY_ID);
		when(itemCategory.getVersion()).thenReturn(DB_ITEM_CATEGORY_VERSION);
		when(itemCategory.getName()).thenReturn(DB_ITEM_CATEGORY_NAME);
		when(itemCategory.getRules()).thenReturn(rules);
		rules.getItemCategories().add(itemCategory);
	}

	@Before
	public void setupForm() {
		when(form.getId()).thenReturn(null);
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getName()).thenReturn(FORM_ITEM_CATEGORY_NAME);
		AddError.to(bindingResult).when(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
		AddError.to(bindingResult).when(form).addErrorSimultaneuosEdit(bindingResult);
	}

	@Before
	public void setupGroup() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.load(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getItemCategories()).thenReturn(new HashSet<ItemCategoryEntity>());
	}

	@Before
	public void setupInstance() {
		instance = new ItemCategoryPersister();
		instance.groupRepository = groupRepository;
		instance.itemCategoryRepository = itemCategoryRepository;
	}
}

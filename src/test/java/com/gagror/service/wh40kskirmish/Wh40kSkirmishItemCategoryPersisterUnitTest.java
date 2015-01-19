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
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryInput;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryRepository;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishItemCategoryPersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final String FORM_ITEM_CATEGORY_NAME = "Item category form";
	private static final Long DB_ITEM_CATEGORY_ID = 5678L;
	private static final String DB_ITEM_CATEGORY_NAME = "Item category DB";
	private static final Long DB_ITEM_CATEGORY_VERSION = 5L;

	Wh40kSkirmishItemCategoryPersister instance;

	@Mock
	Wh40kSkirmishItemCategoryInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	Wh40kSkirmishItemCategoryRepository itemCategoryRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishItemCategoryEntity itemCategory;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<Wh40kSkirmishItemCategoryEntity> savedItemCategory = ArgumentCaptor.forClass(Wh40kSkirmishItemCategoryEntity.class);
		verify(itemCategoryRepository).save(savedItemCategory.capture());
		assertEquals("Wrong name", FORM_ITEM_CATEGORY_NAME, savedItemCategory.getValue().getName());
		assertTrue("Not added to rules", rules.getItemCategories().contains(savedItemCategory.getValue()));
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(itemCategoryRepository, never()).save(any(Wh40kSkirmishItemCategoryEntity.class));
	}

	@Test(expected=IllegalStateException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenItemCategoryExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		verify(itemCategoryRepository, never()).save(any(Wh40kSkirmishItemCategoryEntity.class));
		verify(itemCategory).setName(FORM_ITEM_CATEGORY_NAME);
	}

	@Test
	public void save_existing_bindingError() {
		whenItemCategoryExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(itemCategory, never()).setName(anyString());
		verify(itemCategoryRepository, never()).save(any(Wh40kSkirmishItemCategoryEntity.class));
	}

	@Test(expected=IllegalStateException.class)
	public void save_existing_wrongGroupType() {
		whenItemCategoryExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=IllegalStateException.class)
	public void save_existing_notFoundInGroup() {
		whenItemCategoryExists();
		rules.getItemCategories().remove(itemCategory);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenItemCategoryExists();
		when(form.getVersion()).thenReturn(DB_ITEM_CATEGORY_VERSION - 1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(itemCategory, never()).setName(anyString());
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
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getName()).thenReturn(FORM_ITEM_CATEGORY_NAME);
	}

	@Before
	public void setupGroup() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getItemCategories()).thenReturn(new HashSet<Wh40kSkirmishItemCategoryEntity>());
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishItemCategoryPersister();
		instance.groupRepository = groupRepository;
		instance.itemCategoryRepository = itemCategoryRepository;
	}
}

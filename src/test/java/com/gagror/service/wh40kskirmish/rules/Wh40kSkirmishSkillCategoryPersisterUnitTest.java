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

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryInput;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryRepository;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishSkillCategoryPersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final String FORM_SKILL_CATEGORY_NAME = "Skill category form";
	private static final Long DB_SKILL_CATEGORY_ID = 5678L;
	private static final String DB_SKILL_CATEGORY_NAME = "Skill category DB";
	private static final Long DB_SKILL_CATEGORY_VERSION = 5L;

	Wh40kSkirmishSkillCategoryPersister instance;

	@Mock
	Wh40kSkirmishSkillCategoryInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	Wh40kSkirmishSkillCategoryRepository skillCategoryRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishSkillCategoryEntity skillCategory;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<Wh40kSkirmishSkillCategoryEntity> savedSkillCategory = ArgumentCaptor.forClass(Wh40kSkirmishSkillCategoryEntity.class);
		verify(skillCategoryRepository).save(savedSkillCategory.capture());
		assertEquals("Wrong name", FORM_SKILL_CATEGORY_NAME, savedSkillCategory.getValue().getName());
		assertTrue("Not added to rules", rules.getSkillCategories().contains(savedSkillCategory.getValue()));
	}

	@Test
	public void save_new_nameNotUnique() {
		whenAnotherCategoryWithSameNameExists();
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(skillCategoryRepository, never()).save(any(Wh40kSkirmishSkillCategoryEntity.class));
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(skillCategoryRepository, never()).save(any(Wh40kSkirmishSkillCategoryEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenSkillCategoryExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		verify(skillCategoryRepository, never()).save(any(Wh40kSkirmishSkillCategoryEntity.class));
		verify(skillCategory).setName(FORM_SKILL_CATEGORY_NAME);
	}

	@Test
	public void save_existing_nameNotUnique() {
		whenSkillCategoryExists();
		whenAnotherCategoryWithSameNameExists();
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(skillCategory, never()).setName(anyString());
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_existing_bindingError() {
		whenSkillCategoryExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(skillCategory, never()).setName(anyString());
		verify(skillCategoryRepository, never()).save(any(Wh40kSkirmishSkillCategoryEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_existing_wrongGroupType() {
		whenSkillCategoryExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_notFoundInGroup() {
		whenSkillCategoryExists();
		rules.getSkillCategories().remove(skillCategory);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenSkillCategoryExists();
		when(form.getVersion()).thenReturn(DB_SKILL_CATEGORY_VERSION - 1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(skillCategory, never()).setName(anyString());
	}

	protected void whenAnotherCategoryWithSameNameExists() {
		final Wh40kSkirmishSkillCategoryEntity anotherCategory = mock(Wh40kSkirmishSkillCategoryEntity.class);
		when(anotherCategory.getId()).thenReturn(DB_SKILL_CATEGORY_ID + 1);
		when(anotherCategory.getName()).thenReturn(FORM_SKILL_CATEGORY_NAME);
		rules.getSkillCategories().add(anotherCategory);
	}

	protected void whenSkillCategoryExists() {
		when(form.getId()).thenReturn(DB_SKILL_CATEGORY_ID);
		when(form.getVersion()).thenReturn(DB_SKILL_CATEGORY_VERSION);
		when(skillCategory.getId()).thenReturn(DB_SKILL_CATEGORY_ID);
		when(skillCategory.getVersion()).thenReturn(DB_SKILL_CATEGORY_VERSION);
		when(skillCategory.getName()).thenReturn(DB_SKILL_CATEGORY_NAME);
		when(skillCategory.getRules()).thenReturn(rules);
		rules.getSkillCategories().add(skillCategory);
	}

	@Before
	public void setupForm() {
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getName()).thenReturn(FORM_SKILL_CATEGORY_NAME);
	}

	@Before
	public void setupGroup() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getSkillCategories()).thenReturn(new HashSet<Wh40kSkirmishSkillCategoryEntity>());
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishSkillCategoryPersister();
		instance.groupRepository = groupRepository;
		instance.skillCategoryRepository = skillCategoryRepository;
	}
}

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
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryInput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryRepository;

@RunWith(MockitoJUnitRunner.class)
public class SkillCategoryPersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final String FORM_SKILL_CATEGORY_NAME = "Skill category form";
	private static final Long DB_SKILL_CATEGORY_ID = 5678L;
	private static final String DB_SKILL_CATEGORY_NAME = "Skill category DB";
	private static final Long DB_SKILL_CATEGORY_VERSION = 5L;

	SkillCategoryPersister instance;

	@Mock
	SkillCategoryInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	SkillCategoryRepository skillCategoryRepository;

	@Mock
	GroupEntity group;

	@Mock
	SkillCategoryEntity skillCategory;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		final ArgumentCaptor<SkillCategoryEntity> savedSkillCategory = ArgumentCaptor.forClass(SkillCategoryEntity.class);
		verify(skillCategoryRepository).save(savedSkillCategory.capture());
		assertEquals("Wrong name", FORM_SKILL_CATEGORY_NAME, savedSkillCategory.getValue().getName());
		assertTrue("Not added to rules", rules.getSkillCategories().contains(savedSkillCategory.getValue()));
	}

	@Test
	public void save_new_nameNotUnique() {
		whenAnotherCategoryWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(skillCategoryRepository, never()).save(any(SkillCategoryEntity.class));
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(skillCategoryRepository, never()).save(any(SkillCategoryEntity.class));
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
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		verify(skillCategoryRepository, never()).save(any(SkillCategoryEntity.class));
		verify(skillCategory).setName(FORM_SKILL_CATEGORY_NAME);
	}

	@Test
	public void save_existing_nameNotUnique() {
		whenSkillCategoryExists();
		whenAnotherCategoryWithSameNameExists();
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
		verify(skillCategoryRepository, never()).save(any(SkillCategoryEntity.class));
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
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(skillCategory, never()).setName(anyString());
	}

	protected void whenAnotherCategoryWithSameNameExists() {
		final SkillCategoryEntity anotherCategory = mock(SkillCategoryEntity.class);
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
		when(form.getId()).thenReturn(null);
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getName()).thenReturn(FORM_SKILL_CATEGORY_NAME);
		AddError.to(bindingResult).when(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
		AddError.to(bindingResult).when(form).addErrorSimultaneuosEdit(bindingResult);
	}

	@Before
	public void setupGroup() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.load(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getSkillCategories()).thenReturn(new HashSet<SkillCategoryEntity>());
	}

	@Before
	public void setupInstance() {
		instance = new SkillCategoryPersister();
		instance.groupRepository = groupRepository;
		instance.skillCategoryRepository = skillCategoryRepository;
	}
}

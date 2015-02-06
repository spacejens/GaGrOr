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
import com.gagror.data.wh40kskirmish.rules.skills.SkillEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillInput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillRepository;

@RunWith(MockitoJUnitRunner.class)
public class SkillPersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long SKILL_CATEGORY_ID = 5789L;
	private static final String FORM_SKILL_NAME = "Skill form";
	private static final Long DB_SKILL_ID = 11L;
	private static final String DB_SKILL_NAME = "Skill DB";
	private static final Long DB_SKILL_VERSION = 5L;

	SkillPersister instance;

	@Mock
	SkillInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	SkillRepository skillRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	SkillCategoryEntity skillCategory;

	@Mock
	SkillEntity skill;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		final ArgumentCaptor<SkillEntity> savedSkill = ArgumentCaptor.forClass(SkillEntity.class);
		verify(skillRepository).save(savedSkill.capture());
		assertEquals("Wrong name", FORM_SKILL_NAME, savedSkill.getValue().getName());
		assertTrue("Not added to skill category", skillCategory.getSkills().contains(savedSkill.getValue()));
	}

	@Test
	public void save_new_nameNotUnique() {
		whenAnotherSkillWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(skillRepository, never()).save(any(SkillEntity.class));
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(skillRepository, never()).save(any(SkillEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_new_skillCategoryNotFound() {
		rules.getSkillCategories().remove(skillCategory);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenSkillExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		verify(skillRepository, never()).save(any(SkillEntity.class));
		verify(skill).setName(FORM_SKILL_NAME);
	}

	@Test
	public void save_existing_nameNotUnique() {
		whenSkillExists();
		whenAnotherSkillWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(skill, never()).setName(anyString());
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_existing_bindingError() {
		whenSkillExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(skill, never()).setName(anyString());
		verify(skillRepository, never()).save(any(SkillEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_existing_wrongGroupType() {
		whenSkillExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_skillCategoryNotFound() {
		whenSkillExists();
		rules.getSkillCategories().remove(skillCategory);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_notFoundInSkillCategory() {
		whenSkillExists();
		skillCategory.getSkills().remove(skill);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenSkillExists();
		when(form.getVersion()).thenReturn(DB_SKILL_VERSION - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(skill, never()).setName(anyString());
	}

	protected void whenAnotherSkillWithSameNameExists() {
		final SkillCategoryEntity anotherCategory = mock(SkillCategoryEntity.class);
		when(anotherCategory.getId()).thenReturn(SKILL_CATEGORY_ID + 1);
		when(anotherCategory.getSkills()).thenReturn(new HashSet<SkillEntity>());
		rules.getSkillCategories().add(anotherCategory);
		final SkillEntity anotherSkill = mock(SkillEntity.class);
		when(anotherSkill.getId()).thenReturn(DB_SKILL_ID + 1);
		when(anotherSkill.getName()).thenReturn(FORM_SKILL_NAME);
		anotherCategory.getSkills().add(anotherSkill);
	}

	protected void whenSkillExists() {
		when(form.getId()).thenReturn(DB_SKILL_ID);
		when(form.getVersion()).thenReturn(DB_SKILL_VERSION);
		when(skill.getId()).thenReturn(DB_SKILL_ID);
		when(skill.getVersion()).thenReturn(DB_SKILL_VERSION);
		when(skill.getName()).thenReturn(DB_SKILL_NAME);
		when(skill.getSkillCategory()).thenReturn(skillCategory);
		skillCategory.getSkills().add(skill);
	}

	@Before
	public void setupForm() {
		when(form.getId()).thenReturn(null);
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getSkillCategoryId()).thenReturn(SKILL_CATEGORY_ID);
		when(form.getName()).thenReturn(FORM_SKILL_NAME);
		AddError.to(bindingResult).when(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
		AddError.to(bindingResult).when(form).addErrorSimultaneuosEdit(bindingResult);
	}

	@Before
	public void setupContext() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.load(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getSkillCategories()).thenReturn(new HashSet<SkillCategoryEntity>());
		when(skillCategory.getId()).thenReturn(SKILL_CATEGORY_ID);
		when(skillCategory.getSkills()).thenReturn(new HashSet<SkillEntity>());
		when(skillCategory.getRules()).thenReturn(rules);
		rules.getSkillCategories().add(skillCategory);
	}

	@Before
	public void setupInstance() {
		instance = new SkillPersister();
		instance.groupRepository = groupRepository;
		instance.skillRepository = skillRepository;
	}
}

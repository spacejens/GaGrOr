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

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillEntity;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillInput;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillRepository;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishSkillPersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long SKILL_CATEGORY_ID = 5789L;
	private static final String FORM_SKILL_NAME = "Skill form";
	private static final Long DB_SKILL_ID = 11L;
	private static final String DB_SKILL_NAME = "Skill DB";
	private static final Long DB_SKILL_VERSION = 5L;

	Wh40kSkirmishSkillPersister instance;

	@Mock
	Wh40kSkirmishSkillInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	Wh40kSkirmishSkillRepository skillRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	Wh40kSkirmishSkillCategoryEntity skillCategory;

	@Mock
	Wh40kSkirmishSkillEntity skill;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		final ArgumentCaptor<Wh40kSkirmishSkillEntity> savedSkill = ArgumentCaptor.forClass(Wh40kSkirmishSkillEntity.class);
		verify(skillRepository).save(savedSkill.capture());
		assertEquals("Wrong name", FORM_SKILL_NAME, savedSkill.getValue().getName());
		assertTrue("Not added to skill category", skillCategory.getSkills().contains(savedSkill.getValue()));
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(skillRepository, never()).save(any(Wh40kSkirmishSkillEntity.class));
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
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		verify(skillRepository, never()).save(any(Wh40kSkirmishSkillEntity.class));
		verify(skill).setName(FORM_SKILL_NAME);
	}

	@Test
	public void save_existing_bindingError() {
		whenSkillExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(skill, never()).setName(anyString());
		verify(skillRepository, never()).save(any(Wh40kSkirmishSkillEntity.class));
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
		when(bindingResult.hasErrors()).thenReturn(true); // Will be the case when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(skill, never()).setName(anyString());
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
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getSkillCategoryId()).thenReturn(SKILL_CATEGORY_ID);
		when(form.getName()).thenReturn(FORM_SKILL_NAME);
	}

	@Before
	public void setupContext() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getSkillCategories()).thenReturn(new HashSet<Wh40kSkirmishSkillCategoryEntity>());
		when(skillCategory.getId()).thenReturn(SKILL_CATEGORY_ID);
		when(skillCategory.getSkills()).thenReturn(new HashSet<Wh40kSkirmishSkillEntity>());
		when(skillCategory.getRules()).thenReturn(rules);
		rules.getSkillCategories().add(skillCategory);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishSkillPersister();
		instance.groupRepository = groupRepository;
		instance.skillRepository = skillRepository;
	}
}

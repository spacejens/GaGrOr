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

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.validation.BindingResult;

import com.gagror.AddError;
import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelEntity;
import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelInput;
import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeRepository;

@RunWith(MockitoJUnitRunner.class)
public class GangTypePersisterUnitTest {

	private static final Long GROUP_ID = 2135L;

	private static final String FORM_GANG_TYPE_NAME = "Gang type form";
	private static final String FORM_XP_LEVEL_FIRST_NAME = "First level form";
	private static final int FORM_XP_LEVEL_FIRST_XP = 0;
	private static final String FORM_XP_LEVEL_SECOND_NAME = "Second level form";
	private static final int FORM_XP_LEVEL_SECOND_XP = 6;
	private static final String FORM_XP_LEVEL_NEW_NAME = "New level form";
	private static final int FORM_XP_LEVEL_NEW_XP = 16;

	private static final Long DB_GANG_TYPE_ID = 5678L;
	private static final String DB_GANG_TYPE_NAME = "Gang type DB";
	private static final Long DB_GANG_TYPE_VERSION = 5L;
	private static final Long DB_XP_LEVEL_FIRST_ID = 3457L;
	private static final String DB_XP_LEVEL_FIRST_NAME = "First level DB";
	private static final int DB_XP_LEVEL_FIRST_XP = 0;
	private static final Long DB_XP_LEVEL_SECOND_ID = 3525L;
	private static final String DB_XP_LEVEL_SECOND_NAME = "Second level DB";
	private static final int DB_XP_LEVEL_SECOND_XP = 11;

	GangTypePersister instance;

	@Mock
	GangTypeInput form;

	@Mock
	ExperienceLevelInput formExperienceLevelFirst;

	@Mock
	ExperienceLevelInput formExperienceLevelSecond;

	@Mock
	ExperienceLevelInput formExperienceLevelNew;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	GangTypeRepository gangTypeRepository;

	@Mock
	ExperienceLevelRepository experienceLevelRepository;

	@Mock
	GroupEntity group;

	@Mock
	GangTypeEntity gangType;

	@Mock
	ExperienceLevelEntity experienceLevelFirst;

	@Mock
	ExperienceLevelEntity experienceLevelSecond;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Test
	public void save_new_ok() {
		// Only create a single experience level to avoid random save order issues
		form.getExperienceLevels().remove(formExperienceLevelSecond);
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		final ArgumentCaptor<GangTypeEntity> savedGangType = ArgumentCaptor.forClass(GangTypeEntity.class);
		verify(gangTypeRepository).persist(savedGangType.capture());
		assertEquals("Wrong name", FORM_GANG_TYPE_NAME, savedGangType.getValue().getName());
		assertTrue("Not added to rules", rules.getGangTypes().contains(savedGangType.getValue()));
		final ArgumentCaptor<ExperienceLevelEntity> savedExperienceLevel = ArgumentCaptor.forClass(ExperienceLevelEntity.class);
		verify(experienceLevelRepository).save(savedExperienceLevel.capture());
		assertEquals("Wrong experience level name", FORM_XP_LEVEL_FIRST_NAME, savedExperienceLevel.getValue().getName());
		assertEquals("Wrong experience level points", FORM_XP_LEVEL_FIRST_XP, savedExperienceLevel.getValue().getExperiencePoints());
		assertTrue("Experience level not added to gang type", savedGangType.getValue().getExperienceLevels().contains(savedExperienceLevel.getValue()));
	}

	@Test
	public void save_new_nameNotUnique() {
		whenAnotherGangTypeWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(gangTypeRepository, never()).persist(any(GangTypeEntity.class));
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_new_noExperienceLevelStartsAtZero() {
		when(formExperienceLevelFirst.getExperiencePoints()).thenReturn(1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorExperienceLevelsMustStartAtZero(bindingResult);
		verify(gangTypeRepository, never()).persist(any(GangTypeEntity.class));
	}

	@Test
	public void save_new_experienceLevelsNotUnique() {
		when(formExperienceLevelSecond.getExperiencePoints()).thenReturn(FORM_XP_LEVEL_FIRST_XP);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorExperienceLevelsMustBeUnique(bindingResult);
		verify(gangTypeRepository, never()).persist(any(GangTypeEntity.class));
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(gangTypeRepository, never()).persist(any(GangTypeEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenGangTypeExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		verify(gangTypeRepository, never()).persist(any(GangTypeEntity.class));
		verify(gangType).setName(FORM_GANG_TYPE_NAME);
		verify(experienceLevelFirst).setName(FORM_XP_LEVEL_FIRST_NAME);
		verify(experienceLevelFirst).setExperiencePoints(FORM_XP_LEVEL_FIRST_XP);
		verify(experienceLevelSecond).setName(FORM_XP_LEVEL_SECOND_NAME);
		verify(experienceLevelSecond).setExperiencePoints(FORM_XP_LEVEL_SECOND_XP);
	}

	@Test
	public void save_existing_nameNotUnique() {
		whenGangTypeExists();
		whenAnotherGangTypeWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(gangType, never()).setName(anyString());
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_existing_addExperienceLevel_ok() {
		whenGangTypeExists();
		whenNewExperienceLevelInForm();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		final ArgumentCaptor<ExperienceLevelEntity> savedExperienceLevel = ArgumentCaptor.forClass(ExperienceLevelEntity.class);
		verify(experienceLevelRepository).save(savedExperienceLevel.capture());
		assertEquals("Wrong experience level name", FORM_XP_LEVEL_NEW_NAME, savedExperienceLevel.getValue().getName());
		assertEquals("Wrong experience level points", FORM_XP_LEVEL_NEW_XP, savedExperienceLevel.getValue().getExperiencePoints());
		assertTrue("Experience level not added to gang type", gangType.getExperienceLevels().contains(savedExperienceLevel.getValue()));
	}

	@Test
	public void save_existing_removeExperienceLevel_ok() {
		whenGangTypeExists();
		form.getExperienceLevels().remove(formExperienceLevelSecond);
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(bindingResult).hasErrors(); // Should check for form validation errors
		verifyNoMoreInteractions(bindingResult);
		verify(experienceLevelRepository).delete(experienceLevelSecond);
		assertFalse("Failed to remove from gang type", gangType.getExperienceLevels().contains(experienceLevelSecond));
	}

	@Test
	public void save_existing_noExperienceLevelStartsAtZero() {
		whenGangTypeExists();
		when(formExperienceLevelFirst.getExperiencePoints()).thenReturn(1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorExperienceLevelsMustStartAtZero(bindingResult);
		verify(gangType, never()).setName(anyString());
	}

	@Test
	public void save_existing_experienceLevelsNotUnique() {
		whenGangTypeExists();
		when(formExperienceLevelSecond.getExperiencePoints()).thenReturn(FORM_XP_LEVEL_FIRST_XP);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorExperienceLevelsMustBeUnique(bindingResult);
		verify(gangType, never()).setName(anyString());
	}

	@Test
	public void save_existing_bindingError() {
		whenGangTypeExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(gangType, never()).setName(anyString());
		verify(gangTypeRepository, never()).persist(any(GangTypeEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_existing_wrongGroupType() {
		whenGangTypeExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_notFoundInGroup() {
		whenGangTypeExists();
		rules.getGangTypes().remove(gangType);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenGangTypeExists();
		when(form.getVersion()).thenReturn(DB_GANG_TYPE_VERSION - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(gangType, never()).setName(anyString());
	}

	protected void whenAnotherGangTypeWithSameNameExists() {
		final GangTypeEntity anotherGangType = mock(GangTypeEntity.class);
		when(anotherGangType.getId()).thenReturn(DB_GANG_TYPE_ID + 1);
		when(anotherGangType.getName()).thenReturn(FORM_GANG_TYPE_NAME);
		rules.getGangTypes().add(anotherGangType);
	}

	protected void whenGangTypeExists() {
		when(form.getId()).thenReturn(DB_GANG_TYPE_ID);
		when(form.getVersion()).thenReturn(DB_GANG_TYPE_VERSION);
		when(gangType.getId()).thenReturn(DB_GANG_TYPE_ID);
		when(gangType.getVersion()).thenReturn(DB_GANG_TYPE_VERSION);
		when(gangType.getName()).thenReturn(DB_GANG_TYPE_NAME);
		when(gangType.getRules()).thenReturn(rules);
		rules.getGangTypes().add(gangType);
		when(gangType.getExperienceLevels()).thenReturn(new HashSet<ExperienceLevelEntity>());
		when(experienceLevelFirst.getId()).thenReturn(DB_XP_LEVEL_FIRST_ID);
		when(experienceLevelFirst.getName()).thenReturn(DB_XP_LEVEL_FIRST_NAME);
		when(experienceLevelFirst.getExperiencePoints()).thenReturn(DB_XP_LEVEL_FIRST_XP);
		when(experienceLevelFirst.getGangType()).thenReturn(gangType);
		gangType.getExperienceLevels().add(experienceLevelFirst);
		when(experienceLevelSecond.getId()).thenReturn(DB_XP_LEVEL_SECOND_ID);
		when(experienceLevelSecond.getName()).thenReturn(DB_XP_LEVEL_SECOND_NAME);
		when(experienceLevelSecond.getExperiencePoints()).thenReturn(DB_XP_LEVEL_SECOND_XP);
		when(experienceLevelSecond.getGangType()).thenReturn(gangType);
		gangType.getExperienceLevels().add(experienceLevelSecond);
	}

	protected void whenNewExperienceLevelInForm() {
		when(formExperienceLevelNew.getName()).thenReturn(FORM_XP_LEVEL_NEW_NAME);
		when(formExperienceLevelNew.getExperiencePoints()).thenReturn(FORM_XP_LEVEL_NEW_XP);
		form.getExperienceLevels().add(formExperienceLevelNew);
	}

	@Before
	public void setupForm() {
		when(form.getId()).thenReturn(null);
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getName()).thenReturn(FORM_GANG_TYPE_NAME);
		when(form.getExperienceLevels()).thenReturn(new ArrayList<ExperienceLevelInput>());
		when(formExperienceLevelFirst.getName()).thenReturn(FORM_XP_LEVEL_FIRST_NAME);
		when(formExperienceLevelFirst.getExperiencePoints()).thenReturn(FORM_XP_LEVEL_FIRST_XP);
		form.getExperienceLevels().add(formExperienceLevelFirst);
		when(formExperienceLevelSecond.getName()).thenReturn(FORM_XP_LEVEL_SECOND_NAME);
		when(formExperienceLevelSecond.getExperiencePoints()).thenReturn(FORM_XP_LEVEL_SECOND_XP);
		form.getExperienceLevels().add(formExperienceLevelSecond);
		AddError.to(bindingResult).when(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
		AddError.to(bindingResult).when(form).addErrorSimultaneuosEdit(bindingResult);
		AddError.to(bindingResult).when(form).addErrorExperienceLevelsMustBeUnique(bindingResult);
		AddError.to(bindingResult).when(form).addErrorExperienceLevelsMustStartAtZero(bindingResult);
	}

	@Before
	public void setupGroup() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.load(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getGangTypes()).thenReturn(new HashSet<GangTypeEntity>());
	}

	@Before
	public void setupGangTypeRepository() {
		when(gangTypeRepository.persist(any(GangTypeEntity.class))).thenAnswer(new Answer<GangTypeEntity>() {
			@Override
			public GangTypeEntity answer(final InvocationOnMock invocation) throws Throwable {
				return (GangTypeEntity)invocation.getArguments()[0];
			}
		});
	}

	@Before
	public void setupInstance() {
		instance = new GangTypePersister();
		instance.groupRepository = groupRepository;
		instance.gangTypeRepository = gangTypeRepository;
		instance.experienceLevelRepository = experienceLevelRepository;
	}
}

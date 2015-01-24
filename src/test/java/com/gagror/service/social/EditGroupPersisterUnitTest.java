package com.gagror.service.social;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEditInput;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;

@RunWith(MockitoJUnitRunner.class)
public class EditGroupPersisterUnitTest {

	private static final Long GROUP_ID = 123L;
	private static final Long UNKNOWN_GROUP_ID = 456L;
	private static final Long VERSION = 1L;
	private static final String FORM_NAME = "Group name in form";
	private static final boolean FORM_VIEWABLE = true;

	EditGroupPersister instance;

	@Mock
	GroupRepository groupRepository;

	@Mock
	GroupEditInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupEntity group;

	@Test
	public void save_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(group).setName(FORM_NAME);
		verify(group).setViewableByAnyone(FORM_VIEWABLE);
	}

	@Test
	public void save_nonUniqueName_notViewableByAnyone_ok() {
		whenAnotherGroupWithSameNameIsViewableByAnyone();
		when(form.isViewableByAnyone()).thenReturn(false);
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		verify(group).setName(FORM_NAME);
	}

	@Test
	public void save_nonUniqueName_viewableByAnyone() {
		whenAnotherGroupWithSameNameIsViewableByAnyone();
		when(form.isViewableByAnyone()).thenReturn(true);
		when(bindingResult.hasErrors()).thenReturn(true); // Will have errors when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should not have saved successfully", result);
		verify(form).addErrorNameMustBeUniqueWhenViewableByAnyone(bindingResult);
	}

	private void whenAnotherGroupWithSameNameIsViewableByAnyone() {
		final GroupEntity anotherGroup = mock(GroupEntity.class);
		when(anotherGroup.getId()).thenReturn(UNKNOWN_GROUP_ID);
		when(anotherGroup.getName()).thenReturn(FORM_NAME);
		when(groupRepository.findByViewableByAnyone(true)).thenReturn(Collections.singletonList(anotherGroup));
	}

	@Test(expected=DataNotFoundException.class)
	public void save_groupNotFound() {
		when(form.getId()).thenReturn(UNKNOWN_GROUP_ID);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_preExistingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should not have saved successfully", result);
	}

	@Test
	public void save_simultaneousEdit() {
		when(group.getVersion()).thenReturn(VERSION + 1);
		when(bindingResult.hasErrors()).thenReturn(true); // Will have errors when checked
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should not have saved successfully", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
	}

	@Before
	public void setupForm() {
		when(form.getId()).thenReturn(GROUP_ID);
		when(form.getVersion()).thenReturn(VERSION);
		when(form.getName()).thenReturn(FORM_NAME);
		when(form.isViewableByAnyone()).thenReturn(FORM_VIEWABLE);
	}

	@Before
	public void setupEntity() {
		when(group.getVersion()).thenReturn(VERSION);
	}

	@Before
	public void setupRepository() {
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
	}

	@Before
	public void setupInstance() {
		instance = new EditGroupPersister();
		instance.groupRepository = groupRepository;
	}
}

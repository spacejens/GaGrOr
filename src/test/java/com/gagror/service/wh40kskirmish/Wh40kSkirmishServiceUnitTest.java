package com.gagror.service.wh40kskirmish;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.wh40kskirmish.GroupOutput;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.service.social.GroupService;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishServiceUnitTest {

	private static final Long GROUP_ID = 1L;
	private static final String GROUP_NAME = "Group";

	Wh40kSkirmishService instance;

	@Mock
	GroupService groupService;

	@Mock
	GroupRepository groupRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Test
	public void viewGroup_member_ok() {
		final GroupOutput result = instance.viewGroup(GROUP_ID);
		assertEquals("Wrong group name", GROUP_NAME, result.getName());
	}

	@Before
	public void setupGroup() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(group.getName()).thenReturn(GROUP_NAME);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
	}

	@Before
	public void setupGroupService() {
		when(groupRepository.load(GROUP_ID)).thenReturn(group);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishService();
		instance.groupService = groupService;
		instance.groupRepository = groupRepository;
	}
}

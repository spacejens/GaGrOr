package com.gagror.service.wh40kskirmish;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.MemberType;
import com.gagror.data.wh40kskirmish.GroupOutput;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.service.social.GroupService;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishServiceUnitTest {

	private static final Long GROUP_ID = 1L;
	private static final String GROUP_NAME = "Group";
	private static final MemberType MEMBER_TYPE = MemberType.MEMBER;

	Wh40kSkirmishService instance;

	@Mock
	GroupService groupService;

	@Mock
	GroupRepository groupRepository;

	@Mock
	GroupEntity group;

	@Mock
	GroupMemberEntity groupMember;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Test
	public void viewGroup_member_ok() {
		final GroupOutput result = instance.viewGroup(GROUP_ID);
		assertEquals("Wrong group name", GROUP_NAME, result.getName());
		assertEquals("Wrong member type", MEMBER_TYPE, result.getMemberType());
	}

	@Test
	public void viewGroup_notMember_ok() {
		whenNotMember();
		final GroupOutput result = instance.viewGroup(GROUP_ID);
		assertEquals("Wrong group name", GROUP_NAME, result.getName());
		assertNull("Wrong member type", result.getMemberType());
	}

	private void whenNotMember() {
		when(groupService.findGroupMemberForRequestAccount(group)).thenReturn(null);
	}

	@Before
	public void setupGroup() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(group.getName()).thenReturn(GROUP_NAME);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(groupMember.getGroup()).thenReturn(group);
		when(groupMember.getMemberType()).thenReturn(MEMBER_TYPE);
	}

	@Before
	public void setupGroupService() {
		when(groupRepository.load(GROUP_ID)).thenReturn(group);
		when(groupService.findGroupMemberForRequestAccount(group)).thenReturn(groupMember);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishService();
		instance.groupService = groupService;
		instance.groupRepository = groupRepository;
	}
}

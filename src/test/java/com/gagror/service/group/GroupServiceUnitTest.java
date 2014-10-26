package com.gagror.service.group;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupListOutput;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.MemberType;
import com.gagror.service.accesscontrol.AccessControlService;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceUnitTest {

	private static final Long FIRST_GROUP_ID = 11L;
	private static final Long SECOND_GROUP_ID = 22L;
	private static final Long THIRD_GROUP_ID = 33L;
	private static final String FIRST_GROUP_NAME = "First";
	private static final String SECOND_GROUP_NAME = "Second";
	private static final String THIRD_GROUP_NAME = "Third";

	GroupService instance;

	@Mock
	AccessControlService accessControlService;

	@Mock
	AccountEntity requestAccount;

	@Mock
	GroupMemberEntity firstGroupOwner;

	@Mock
	GroupMemberEntity secondGroupMember;

	@Mock
	GroupMemberEntity thirdGroupInvited;

	@Mock
	GroupEntity firstGroup;

	@Mock
	GroupEntity secondGroup;

	@Mock
	GroupEntity thirdGroup;

	@Test
	public void loadGroupList_ok() {
		assertGroups(instance.loadGroupList(), FIRST_GROUP_ID, SECOND_GROUP_ID);
	}

	@Test
	public void loadGroupList_noMemberships() {
		requestAccount.getGroupMemberships().clear();
		assertGroups(instance.loadGroupList());
	}

	private void assertGroups(final List<GroupListOutput> result, final Long... expectedGroupIds) {
		final List<Long> expected = Arrays.asList(expectedGroupIds);
		final List<Long> actual = new ArrayList<>();
		for(final GroupListOutput output : result) {
			actual.add(output.getId());
		}
		assertEquals("Unexpected groups returned", expected, actual);
	}

	@Before
	public void setupRequestAccount() {
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		memberships.add(firstGroupOwner);
		memberships.add(secondGroupMember);
		memberships.add(thirdGroupInvited);
		when(requestAccount.getGroupMemberships()).thenReturn(memberships);
	}

	@Before
	public void setupGroups() {
		when(firstGroup.getId()).thenReturn(FIRST_GROUP_ID);
		when(firstGroup.getName()).thenReturn(FIRST_GROUP_NAME);
		when(firstGroupOwner.getGroup()).thenReturn(firstGroup);
		when(firstGroupOwner.getMemberType()).thenReturn(MemberType.OWNER);
		when(secondGroup.getId()).thenReturn(SECOND_GROUP_ID);
		when(secondGroup.getName()).thenReturn(SECOND_GROUP_NAME);
		when(secondGroupMember.getGroup()).thenReturn(secondGroup);
		when(secondGroupMember.getMemberType()).thenReturn(MemberType.MEMBER);
		when(thirdGroup.getId()).thenReturn(THIRD_GROUP_ID);
		when(thirdGroup.getName()).thenReturn(THIRD_GROUP_NAME);
		when(thirdGroupInvited.getGroup()).thenReturn(thirdGroup);
		when(thirdGroupInvited.getMemberType()).thenReturn(MemberType.INVITED);
	}

	@Before
	public void setupAccessControlService() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(requestAccount);
	}

	@Before
	public void setupInstance() {
		instance = new GroupService();
		instance.accessControlService = accessControlService;
	}
}

package com.gagror.service.accesscontrol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.MemberType;

@RunWith(MockitoJUnitRunner.class)
public class PermissionViewGroupUnitTest {

	private static final Long FIRST_GROUP_ID = 123L;
	private static final Long SECOND_GROUP_ID = 456L;
	private static final Long OTHER_GROUP_ID = 999L;

	private static final Long FIRST_MEMBER_ID = 123L;
	private static final Long SECOND_MEMBER_ID = 456L;

	PermissionViewGroup instance;

	@Mock
	AccountEntity account;

	@Mock
	GroupMemberEntity firstGroupMember;

	@Mock
	GroupEntity firstGroup;

	@Mock
	GroupMemberEntity secondGroupInvited;

	@Mock
	GroupEntity secondGroup;

	@Test
	public void hasPermission_member() {
		assertTrue("Group member should have permission", instance.hasPermission(FIRST_GROUP_ID, account));
	}

	@Test
	public void hasPermission_invited() {
		assertTrue("Invited member should have permission", instance.hasPermission(SECOND_GROUP_ID, account));
	}

	@Test
	public void hasPermission_unknownGroup() {
		assertFalse("Should not have permission to unknown group", instance.hasPermission(OTHER_GROUP_ID, account));
	}

	@Before
	public void setupAccount() {
		when(firstGroup.getId()).thenReturn(FIRST_GROUP_ID);
		when(firstGroupMember.getId()).thenReturn(FIRST_MEMBER_ID);
		when(firstGroupMember.getMemberType()).thenReturn(MemberType.MEMBER);
		when(firstGroupMember.getGroup()).thenReturn(firstGroup);
		when(secondGroup.getId()).thenReturn(SECOND_GROUP_ID);
		when(secondGroupInvited.getId()).thenReturn(SECOND_MEMBER_ID);
		when(secondGroupInvited.getMemberType()).thenReturn(MemberType.INVITED);
		when(secondGroupInvited.getGroup()).thenReturn(secondGroup);
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		memberships.add(firstGroupMember);
		memberships.add(secondGroupInvited);
		when(account.getGroupMemberships()).thenReturn(memberships);
	}

	@Before
	public void setupInstance() {
		instance = new PermissionViewGroup();
	}
}

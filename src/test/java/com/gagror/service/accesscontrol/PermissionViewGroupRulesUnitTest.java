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
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.MemberType;

@RunWith(MockitoJUnitRunner.class)
public class PermissionViewGroupRulesUnitTest {

	private static final Long FIRST_GROUP_ID = 123L;
	private static final Long SECOND_GROUP_ID = 456L;
	private static final Long THIRD_GROUP_ID = 789L;
	private static final Long OTHER_GROUP_ID = 999L;

	private static final Long FIRST_MEMBER_ID = 123L;
	private static final Long SECOND_MEMBER_ID = 456L;

	PermissionViewGroupRules instance;

	@Mock
	GroupRepository groupRepository;

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

	@Mock
	GroupEntity thirdGroup;

	@Test
	public void hasPermission_member() {
		assertTrue("Group member should have permission", instance.hasPermission(FIRST_GROUP_ID, account));
	}

	@Test
	public void hasPermission_invited() {
		assertTrue("Invited member should have permission", instance.hasPermission(SECOND_GROUP_ID, account));
	}

	@Test
	public void hasPermission_notMember() {
		assertFalse("Non-member should not have permission", instance.hasPermission(THIRD_GROUP_ID, account));
	}

	@Test
	public void hasPermission_notLoggedIn() {
		assertFalse("Not logged in, should not have permission", instance.hasPermission(THIRD_GROUP_ID, null));
	}

	@Test
	public void hasPermission_viewableByAnyone() {
		when(thirdGroup.isViewableByAnyone()).thenReturn(true);
		assertFalse("Non-member should not have permission", instance.hasPermission(THIRD_GROUP_ID, account));
	}

	@Test
	public void hasPermission_viewableByAnyone_notLoggedIn() {
		when(thirdGroup.isViewableByAnyone()).thenReturn(true);
		assertFalse("Not logged in, should not have permission", instance.hasPermission(THIRD_GROUP_ID, null));
	}

	@Test
	public void hasPermission_unknownGroup() {
		assertFalse("Should not have permission to unknown group", instance.hasPermission(OTHER_GROUP_ID, account));
	}

	@Before
	public void setup() {
		setupAccount();
		setupGroup(firstGroup, FIRST_GROUP_ID);
		setupGroupMember(firstGroup, firstGroupMember, FIRST_MEMBER_ID, MemberType.MEMBER);
		setupGroup(secondGroup, SECOND_GROUP_ID);
		setupGroupMember(secondGroup, secondGroupInvited, SECOND_MEMBER_ID, MemberType.INVITED);
		setupGroup(thirdGroup, THIRD_GROUP_ID);
	}

	private void setupAccount() {
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		when(account.getGroupMemberships()).thenReturn(memberships);
	}

	private void setupGroup(final GroupEntity group, final Long id) {
		when(group.getId()).thenReturn(id);
		when(group.isViewableByAnyone()).thenReturn(false);
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		when(group.getGroupMemberships()).thenReturn(memberships);
		when(groupRepository.findOne(id)).thenReturn(group);
	}

	private void setupGroupMember(final GroupEntity group, final GroupMemberEntity member, final Long id, final MemberType memberType) {
		when(member.getId()).thenReturn(id);
		when(member.getMemberType()).thenReturn(memberType);
		when(member.getGroup()).thenReturn(group);
		account.getGroupMemberships().add(member);
		group.getGroupMemberships().add(member);
	}

	@Before
	public void setupInstance() {
		instance = new PermissionViewGroupRules();
		instance.groupRepository = groupRepository;
	}
}

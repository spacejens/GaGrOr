package com.gagror.service.social;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.validation.BindingResult;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupCreateInput;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupListOutput;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupMemberRepository;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.GroupViewOutput;
import com.gagror.data.group.MemberType;
import com.gagror.service.accesscontrol.AccessControlService;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceUnitTest {

	private static final Long FIRST_GROUP_ID = 11L;
	private static final Long SECOND_GROUP_ID = 22L;
	private static final Long THIRD_GROUP_ID = 33L;
	private static final Long FOURTH_GROUP_ID = 44L;
	private static final Long ANOTHER_GROUP_ID = 55L;
	private static final String FIRST_GROUP_NAME = "First";
	private static final String SECOND_GROUP_NAME = "Second";
	private static final String THIRD_GROUP_NAME = "Third";
	private static final String FOURTH_GROUP_NAME = "Fourth";
	private static final String ANOTHER_GROUP_NAME = "Another";
	private static final String NEW_GROUP_NAME = "New group";
	private static final Long FIRST_MEMBERSHIP_ID = 111L;
	private static final Long SECOND_MEMBERSHIP_ID = 222L;
	private static final Long THIRD_MEMBERSHIP_ID = 333L;
	private static final Long FOURTH_MEMBERSHIP_ID = 444L;

	GroupService instance;

	@Mock
	AccessControlService accessControlService;

	@Mock
	GroupRepository groupRepository;

	@Mock
	GroupMemberRepository groupMemberRepository;

	@Mock
	AccountEntity requestAccount;

	@Mock
	GroupMemberEntity firstGroupOwner;

	@Mock
	GroupMemberEntity secondGroupMember;

	@Mock
	GroupMemberEntity thirdGroupInvited;

	@Mock
	GroupMemberEntity fourthGroupInvited;

	@Mock
	GroupEntity firstGroup;

	@Mock
	GroupEntity secondGroup;

	@Mock
	GroupEntity thirdGroup;

	@Mock
	GroupEntity fourthGroup;

	@Mock
	GroupEntity anotherGroup;

	@Mock
	GroupCreateInput groupCreateForm;

	@Mock
	BindingResult bindingResult;

	@Test
	public void loadGroupList_ok() {
		assertGroups(instance.loadGroupList(), FIRST_GROUP_ID, SECOND_GROUP_ID);
	}

	@Test
	public void loadGroupList_noMemberships() {
		requestAccount.getGroupMemberships().clear();
		assertGroups(instance.loadGroupList());
	}

	@Test
	public void loadInvitationsList_ok() {
		assertGroups(instance.loadInvitationsList(), FOURTH_GROUP_ID, THIRD_GROUP_ID);
	}

	@Test
	public void loadInvitationsList_noInvitations() {
		requestAccount.getGroupMemberships().clear();
		assertGroups(instance.loadInvitationsList());
	}

	@Test
	public void createGroup_ok() {
		instance.createGroup(groupCreateForm, bindingResult);
		final ArgumentCaptor<GroupEntity> group = ArgumentCaptor.forClass(GroupEntity.class);
		verify(groupRepository).save(group.capture());
		assertEquals("Wrong name of created group", NEW_GROUP_NAME, group.getValue().getName());
		final ArgumentCaptor<GroupMemberEntity> groupMember = ArgumentCaptor.forClass(GroupMemberEntity.class);
		verify(groupMemberRepository).save(groupMember.capture());
		assertSame("Group member should be for saved group", group.getValue(), groupMember.getValue().getGroup());
		assertSame("Group member should be for request account", requestAccount, groupMember.getValue().getAccount());
		assertEquals("Group member should be owner", MemberType.OWNER, groupMember.getValue().getMemberType());
		assertTrue("Group should have member", group.getValue().getGroupMemberships().contains(groupMember.getValue()));
		assertTrue("Account should be member", requestAccount.getGroupMemberships().contains(groupMember.getValue()));
	}

	@Test
	public void viewGroup_owner() {
		viewGroup_ok(FIRST_GROUP_ID, FIRST_GROUP_NAME, MemberType.OWNER);
	}

	@Test
	public void viewGroup_member() {
		viewGroup_ok(SECOND_GROUP_ID, SECOND_GROUP_NAME, MemberType.MEMBER);
	}

	@Test
	public void viewGroup_invited() {
		viewGroup_ok(THIRD_GROUP_ID, THIRD_GROUP_NAME, MemberType.INVITED);
	}

	@Test
	public void viewGroup_notMember() {
		viewGroup_ok(ANOTHER_GROUP_ID, ANOTHER_GROUP_NAME, null);
	}

	private void viewGroup_ok(final Long id, final String expectedName, final MemberType expectedMemberType) {
		final GroupViewOutput result = instance.viewGroup(id);
		assertEquals("Wrong group found", expectedName, result.getName());
		assertEquals("Wrong membership type", expectedMemberType, result.getMemberType());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewGroup_notFound() {
		instance.viewGroup(34578095L);
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
	public void setupGroupCreateForm() {
		when(groupCreateForm.getName()).thenReturn(NEW_GROUP_NAME);
	}

	@Before
	public void setupGroupRepository() {
		when(groupRepository.save(any(GroupEntity.class))).thenAnswer(new Answer<GroupEntity>(){
			@Override
			public GroupEntity answer(final InvocationOnMock invocation) throws Throwable {
				final GroupEntity group = (GroupEntity)invocation.getArguments()[0];
				return group;
			}
		});
	}

	@Before
	public void setupGroups() {
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		when(requestAccount.getGroupMemberships()).thenReturn(memberships);
		mockGroup(firstGroup, FIRST_GROUP_ID, FIRST_GROUP_NAME);
		mockGroupMember(firstGroupOwner, firstGroup, FIRST_MEMBERSHIP_ID, MemberType.OWNER, requestAccount);
		mockGroup(secondGroup, SECOND_GROUP_ID, SECOND_GROUP_NAME);
		mockGroupMember(secondGroupMember, secondGroup, SECOND_MEMBERSHIP_ID, MemberType.MEMBER, requestAccount);
		mockGroup(thirdGroup, THIRD_GROUP_ID, THIRD_GROUP_NAME);
		mockGroupMember(thirdGroupInvited, thirdGroup, THIRD_MEMBERSHIP_ID, MemberType.INVITED, requestAccount);
		mockGroup(fourthGroup, FOURTH_GROUP_ID, FOURTH_GROUP_NAME);
		mockGroupMember(fourthGroupInvited, fourthGroup, FOURTH_MEMBERSHIP_ID, MemberType.INVITED, requestAccount);
		mockGroup(anotherGroup, ANOTHER_GROUP_ID, ANOTHER_GROUP_NAME);
	}

	private void mockGroup(final GroupEntity group, final Long id, final String name) {
		when(group.getId()).thenReturn(id);
		when(group.getName()).thenReturn(name);
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		when(group.getGroupMemberships()).thenReturn(memberships);
		when(groupRepository.findOne(id)).thenReturn(group);
	}

	private void mockGroupMember(final GroupMemberEntity member, final GroupEntity group, final Long id, final MemberType memberType, final AccountEntity account) {
		when(member.getId()).thenReturn(id);
		when(member.getGroup()).thenReturn(group);
		when(member.getAccount()).thenReturn(account);
		when(member.getMemberType()).thenReturn(memberType);
		group.getGroupMemberships().add(member);
		account.getGroupMemberships().add(member);
	}

	@Before
	public void setupAccessControlService() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(requestAccount);
	}

	@Before
	public void setupInstance() {
		instance = new GroupService();
		instance.accessControlService = accessControlService;
		instance.groupRepository = groupRepository;
		instance.groupMemberRepository = groupMemberRepository;
	}
}
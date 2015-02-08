package com.gagror.service.wh40kskirmish;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.MemberType;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishPermissionGangPlayerUnitTest {

	private static final Long GANG_ID = 12L;
	private static final Long ACCOUNT_ID_PLAYER = 34L;
	private static final Long ACCOUNT_ID_GROUP_OWNER = 56L;
	private static final Long ACCOUNT_ID_OTHER = 78L;

	Wh40kSkirmishPermissionGangPlayer instance;

	@Mock
	GangRepository gangRepository;

	@Mock
	GangEntity gang;

	@Mock
	GroupEntity group;

	@Mock
	AccountEntity player;

	@Mock
	AccountEntity groupOwner;

	@Mock
	AccountEntity anotherAccount;

	@Test
	public void hasPermission_player_ok() {
		assertTrue("Player should have permission", instance.hasPermission(GANG_ID, player));
	}

	@Test
	public void hasPermission_playerNoLongerMember_notOk() {
		setupAccount(player, ACCOUNT_ID_PLAYER, null);
		assertFalse("Player should not have permission when no longer member", instance.hasPermission(GANG_ID, player));
	}

	@Test
	public void hasPermission_playerOnlyInvited_notOk() {
		setupAccount(player, ACCOUNT_ID_PLAYER, MemberType.INVITED);
		assertFalse("Player should not have permission when only invited", instance.hasPermission(GANG_ID, player));
	}

	@Test
	public void hasPermission_groupOwner_ok() {
		assertTrue("Group owner should have permission", instance.hasPermission(GANG_ID, groupOwner));
	}

	@Test
	public void hasPermission_anotherAccount_notOk() {
		assertFalse("Other accounts should not have permission", instance.hasPermission(GANG_ID, anotherAccount));
	}

	@Test(expected=DataNotFoundException.class)
	public void hasPermission_gangNotFound() {
		doThrow(DataNotFoundException.class).when(gangRepository).load(GANG_ID);
		instance.hasPermission(GANG_ID, player);
	}

	@Before
	public void setup() {
		when(gang.getId()).thenReturn(GANG_ID);
		when(gang.getPlayer()).thenReturn(player);
		when(gang.getGroup()).thenReturn(group);
		when(gangRepository.load(GANG_ID)).thenReturn(gang);
		when(group.getGroupMemberships()).thenReturn(new HashSet<GroupMemberEntity>());
		setupAccount(player, ACCOUNT_ID_PLAYER, MemberType.MEMBER);
		setupAccount(groupOwner, ACCOUNT_ID_GROUP_OWNER, MemberType.OWNER);
		setupAccount(anotherAccount, ACCOUNT_ID_OTHER, MemberType.MEMBER);
	}

	private void setupAccount(final AccountEntity account, final Long accountId, final MemberType memberType) {
		when(account.getId()).thenReturn(accountId);
		// Try to find an existing membership to change
		GroupMemberEntity membership = null;
		for(final GroupMemberEntity existingMembership : group.getGroupMemberships()) {
			if(existingMembership.getAccount().equals(account)) {
				membership = existingMembership;
			}
		}
		// If account should not be member, set that up
		if(null == memberType) {
			// Remove membership if it already exists
			if(null != membership) {
				group.getGroupMemberships().remove(membership);
			}
			return;
		}
		// Create a new membership if needed
		if(null == membership) {
			membership = mock(GroupMemberEntity.class);
			when(membership.getId()).thenReturn(accountId); // Reuse the account ID to be unique
			when(membership.getGroup()).thenReturn(group);
			when(membership.getAccount()).thenReturn(account);
			group.getGroupMemberships().add(membership);
		}
		// Set the membership level
		when(membership.getMemberType()).thenReturn(memberType);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishPermissionGangPlayer();
		instance.gangRepository = gangRepository;
	}
}

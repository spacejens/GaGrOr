package com.gagror.data.account;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;

import com.gagror.data.Output;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.group.PlayerOwnedOutput;

public class CurrentUserOutput implements Output {

	@Getter
	private final AccountReferenceOutput account;

	@Getter
	private final Set<Long> groupsInvited;

	@Getter
	private final Set<Long> groupsMember;

	@Getter
	private final Set<Long> groupsOwner;

	public CurrentUserOutput(final AccountEntity requestAccount) {
		account = new AccountReferenceOutput(requestAccount);
		final Set<Long> tempGroupsInvited = new HashSet<>();
		final Set<Long> tempGroupsMember = new HashSet<>();
		final Set<Long> tempGroupsOwner = new HashSet<>();
		for(final GroupMemberEntity membership : requestAccount.getGroupMemberships()) {
			if(membership.getMemberType().isInvitation()) {
				tempGroupsInvited.add(membership.getGroup().getId());
			}
			if(membership.getMemberType().isMember()) {
				tempGroupsMember.add(membership.getGroup().getId());
			}
			if(membership.getMemberType().isOwner()) {
				tempGroupsOwner.add(membership.getGroup().getId());
			}
		}
		groupsInvited = Collections.unmodifiableSet(tempGroupsInvited);
		groupsMember = Collections.unmodifiableSet(tempGroupsMember);
		groupsOwner = Collections.unmodifiableSet(tempGroupsOwner);
	}

	public CurrentUserOutput() {
		account = null;
		groupsInvited = Collections.emptySet();
		groupsMember = Collections.emptySet();
		groupsOwner = Collections.emptySet();
	}

	public boolean is(final AccountReferenceOutput otherAccount) {
		if(null == getAccount()) {
			return false;
		}
		return otherAccount.getId().equals(getAccount().getId());
	}

	public boolean isNot(final AccountReferenceOutput otherAccount) {
		return !is(otherAccount);
	}

	public List<AccountType> getMayEdit() {
		if(isLoggedIn()) {
			return getAccount().getAccountType().getMayEdit();
		}
		return Collections.emptyList();
	}

	public boolean mayEdit(final AccountReferenceOutput otherAccount) {
		if(is(otherAccount)) {
			return true;
		}
		return getMayEdit().contains(otherAccount.getAccountType());
	}

	// TODO Simplify HTML when checking group membership status
	public boolean isInvitedOrMember(final GroupReferenceOutput group) {
		return isInvited(group) || isMember(group);
	}
	public boolean isInvited(final GroupReferenceOutput group) {
		return getGroupsInvited().contains(group.getId());
	}
	public boolean isMember(final GroupReferenceOutput group) {
		return getGroupsMember().contains(group.getId());
	}
	public boolean isOwner(final GroupReferenceOutput group) {
		return getGroupsOwner().contains(group.getId());
	}

	// TODO Simplify HTML when checking if account can act as player
	public boolean canActAsPlayer(final PlayerOwnedOutput data) {
		return isOwner(data.getGroup()) || (is(data.getPlayer()) && isMember(data.getGroup()));
	}

	public boolean isLoggedIn() {
		return null != account;
	}
}

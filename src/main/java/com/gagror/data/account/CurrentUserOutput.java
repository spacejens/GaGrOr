package com.gagror.data.account;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

import com.gagror.data.Output;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.group.MemberType;
import com.gagror.data.group.PlayerOwnedOutput;

public class CurrentUserOutput implements Output {

	@Getter
	private final AccountReferenceOutput account;

	@Getter
	private final Map<Long, MemberType> groupMemberships;

	public CurrentUserOutput(final AccountEntity requestAccount) {
		account = new AccountReferenceOutput(requestAccount);
		final Map<Long, MemberType> tempGroupMemberships = new HashMap<>();
		for(final GroupMemberEntity membership : requestAccount.getGroupMemberships()) {
			tempGroupMemberships.put(membership.getGroup().getId(), membership.getMemberType());
		}
		groupMemberships = Collections.unmodifiableMap(tempGroupMemberships);
	}

	public CurrentUserOutput() {
		account = null;
		groupMemberships = Collections.emptyMap();
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

	public MemberType getMemberType(final GroupReferenceOutput group) {
		return getGroupMemberships().get(group.getId());
	}
	public boolean isInvitedOrMember(final GroupReferenceOutput group) {
		return isInvited(group) || isMember(group);
	}
	public boolean isInvited(final GroupReferenceOutput group) {
		return getGroupMemberships().containsKey(group.getId())
				&& getGroupMemberships().get(group.getId()).isInvitation();
	}
	public boolean isMember(final GroupReferenceOutput group) {
		return getGroupMemberships().containsKey(group.getId())
				&& getGroupMemberships().get(group.getId()).isMember();
	}
	public boolean isOwner(final GroupReferenceOutput group) {
		return getGroupMemberships().containsKey(group.getId())
				&& getGroupMemberships().get(group.getId()).isOwner();
	}

	public boolean canActAsPlayer(final PlayerOwnedOutput data) {
		return isOwner(data.getGroup()) || (is(data.getPlayer()) && isMember(data.getGroup()));
	}

	public boolean isLoggedIn() {
		return null != account;
	}
}

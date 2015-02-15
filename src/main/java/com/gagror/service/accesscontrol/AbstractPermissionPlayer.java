package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import com.gagror.data.Identifiable;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.PlayerOwned;

@CommonsLog
public abstract class AbstractPermissionPlayer<E extends Identifiable<Long> & PlayerOwned>
extends AbstractGagrorPermissionLongId<E> {

	protected AbstractPermissionPlayer(final String name, final Class<E> targetClass) {
		super(name, targetClass);
	}

	@Override
	public final boolean hasPermission(final Long id, final AccountEntity account) {
		final E target = loadTarget(id);
		// Find the group membership of the account
		GroupMemberEntity accountMembership = null;
		for(final GroupMemberEntity groupMembership : target.getGroup().getGroupMemberships()) {
			if(groupMembership.getAccount().equals(account)) {
				accountMembership = groupMembership;
			}
		}
		if(null == accountMembership || ! accountMembership.getMemberType().isMember()) {
			log.debug(String.format("Account %s is not a member of group of %s", account, target));
			return false;
		}
		// Allow if player
		if(target.getPlayer().equals(account)) {
			log.debug(String.format("Account %s is player of %s", account, target));
			return true;
		}
		// Allow if group owner
		if(accountMembership.getMemberType().isOwner()) {
			log.debug(String.format("Account %s is group owner for group of %s", account, target));
			return true;
		}
		// Disallow for everyone else
		return false;
	}

	protected abstract E loadTarget(final Long id);
}

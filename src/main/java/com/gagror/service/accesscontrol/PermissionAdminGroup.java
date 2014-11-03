package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.stereotype.Component;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;

@CommonsLog
@Component
public class PermissionAdminGroup extends AbstractGagrorPermissionLongId<GroupEntity> {

	public PermissionAdminGroup() {
		super("adminGroup", GroupEntity.class);
	}

	@Override
	protected boolean hasPermission(final Long id, final AccountEntity account) {
		for(final GroupMemberEntity membership : account.getGroupMemberships()) {
			if(id.equals(membership.getGroup().getId()) && membership.getMemberType().isOwner()) {
				log.debug(String.format("Account %s has permission to administrate group %s", account, membership.getGroup()));
				return true;
			}
		}
		log.debug(String.format("Account %s doesn't have permission to administrate group %d", account, id));
		return false;
	}
}

package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.stereotype.Component;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;

@CommonsLog
@Component
public class PermissionViewGroup extends AbstractGagrorPermissionLongId<GroupEntity> {

	public PermissionViewGroup() {
		super("viewGroup", GroupEntity.class);
	}

	@Override
	protected boolean hasPermission(final Long id, final AccountEntity account) {
		// TODO First, load the group directly and check if it is viewable by anyone
		for(final GroupMemberEntity membership : account.getGroupMemberships()) {
			if(id.equals(membership.getGroup().getId())) {
				log.debug(String.format("Account %s has permission to view group %s", account, membership.getGroup()));
				return true;
			}
		}
		log.debug(String.format("Account %s doesn't have permission to view group %d", account, id));
		return false;
	}
}

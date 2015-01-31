package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupRepository;

@CommonsLog
public abstract class AbstractPermissionViewGroup extends AbstractGagrorPermissionLongId<GroupEntity> {

	@Autowired
	GroupRepository groupRepository;

	final boolean viewableByAnyoneApplicable;

	public AbstractPermissionViewGroup(final String name, final boolean viewableByAnyoneApplicable) {
		super(name, GroupEntity.class);
		this.viewableByAnyoneApplicable = viewableByAnyoneApplicable;
	}

	@Override
	protected final boolean hasPermission(final Long id, final AccountEntity account) {
		// First, load the group and check if it is viewable by anyone
		final GroupEntity group = groupRepository.findOne(id);
		if(null == group) {
			return false;
		}
		if(viewableByAnyoneApplicable && group.isViewableByAnyone()) {
			return true;
		}
		// If group is not public and the user is not logged in, decline the permission
		if(null == account) {
			return false;
		}
		// Check the memberships
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

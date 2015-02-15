package com.gagror.data.group;

import lombok.Getter;

import com.gagror.data.AbstractEditableNamedEntityOutput;
import com.gagror.data.URLOutput;

public class GroupReferenceOutput
extends AbstractEditableNamedEntityOutput
implements URLOutput {

	@Getter
	private final GroupType groupType;

	@Getter
	private final MemberType memberType;

	@Getter
	private final boolean viewableByAnyone;

	public GroupReferenceOutput(final GroupEntity group) {
		super(group);
		groupType = group.getGroupType();
		memberType = null;
		viewableByAnyone = group.isViewableByAnyone();
	}

	public GroupReferenceOutput(final GroupMemberEntity membership) {
		super(membership.getGroup());
		final GroupEntity group = membership.getGroup();
		groupType = group.getGroupType();
		memberType = membership.getMemberType();
		viewableByAnyone = group.isViewableByAnyone();
	}

	@Override
	public String getUrl() {
		return String.format("%s/%d", getGroupType().getGroupUrl(), getId());
	}
}

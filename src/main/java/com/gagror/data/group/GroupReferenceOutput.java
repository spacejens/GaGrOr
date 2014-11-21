package com.gagror.data.group;

import lombok.Getter;
import lombok.ToString;

import com.gagror.data.AbstractEntityOutput;

@ToString(of="name", callSuper=true)
public class GroupReferenceOutput
extends AbstractEntityOutput
implements Comparable<GroupReferenceOutput> {

	@Getter
	private final String name;

	@Getter
	private final GroupType groupType;

	@Getter
	private final MemberType memberType;

	@Getter
	private final boolean viewableByAnyone;

	public GroupReferenceOutput(final GroupEntity group) {
		super(group);
		name = group.getName();
		groupType = group.getGroupType();
		memberType = null;
		viewableByAnyone = group.isViewableByAnyone();
	}

	public GroupReferenceOutput(final GroupMemberEntity membership) {
		super(membership.getGroup());
		final GroupEntity group = membership.getGroup();
		name = group.getName();
		groupType = group.getGroupType();
		memberType = membership.getMemberType();
		viewableByAnyone = group.isViewableByAnyone();
	}

	@Override
	public int compareTo(final GroupReferenceOutput other) {
		return getName().compareTo(other.getName());
	}
}

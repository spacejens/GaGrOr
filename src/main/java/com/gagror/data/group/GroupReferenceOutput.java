package com.gagror.data.group;

import lombok.Data;

@Data
public class GroupReferenceOutput implements Comparable<GroupReferenceOutput> {

	private final Long id;

	private final String name;

	private final MemberType memberType;

	public GroupReferenceOutput(final GroupEntity group) {
		id = group.getId();
		name = group.getName();
		memberType = null;
	}

	public GroupReferenceOutput(final GroupMemberEntity membership) {
		final GroupEntity group = membership.getGroup();
		id = group.getId();
		name = group.getName();
		memberType = membership.getMemberType();
	}

	@Override
	public int compareTo(final GroupReferenceOutput other) {
		return getName().compareTo(other.getName());
	}
}

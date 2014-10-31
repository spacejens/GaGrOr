package com.gagror.data.group;

import lombok.Data;

@Data
public class GroupReferenceOutput implements Comparable<GroupReferenceOutput> {

	private final Long id;

	private final String name;

	public GroupReferenceOutput(final GroupEntity entity) {
		id = entity.getId();
		name = entity.getName();
	}

	@Override
	public int compareTo(final GroupReferenceOutput other) {
		return getName().compareTo(other.getName());
	}
}

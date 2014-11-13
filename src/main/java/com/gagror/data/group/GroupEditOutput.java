package com.gagror.data.group;

import lombok.Getter;

public class GroupEditOutput
extends GroupReferenceOutput {

	@Getter
	private final Long version;

	public GroupEditOutput(final GroupMemberEntity membership) {
		super(membership);
		version = membership.getGroup().getVersion();
	}
}

package com.gagror.data.group;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class GroupViewOutput extends GroupReferenceOutput {

	private final Date created;

	public GroupViewOutput(final GroupMemberEntity membership) {
		super(membership);
		created = membership.getGroup().getCreated();
	}

	public GroupViewOutput(final GroupEntity group) {
		super(group);
		created = group.getCreated();
	}
}

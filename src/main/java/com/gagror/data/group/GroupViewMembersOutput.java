package com.gagror.data.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

public class GroupViewMembersOutput extends GroupReferenceOutput {

	@Getter
	private final List<GroupMemberOutput> members;

	public GroupViewMembersOutput(final GroupMemberEntity membership) {
		super(membership);
		members = extract(membership.getGroup());
	}

	private List<GroupMemberOutput> extract(final GroupEntity group) {
		final List<GroupMemberOutput> output = new ArrayList<>();
		for(final GroupMemberEntity member : group.getGroupMemberships()) {
			output.add(new GroupMemberOutput(member));
		}
		Collections.sort(output);
		return Collections.unmodifiableList(output);
	}
}

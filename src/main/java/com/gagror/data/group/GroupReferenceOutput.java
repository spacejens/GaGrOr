package com.gagror.data.group;

import lombok.Data;

@Data
public class GroupReferenceOutput implements Comparable<GroupReferenceOutput> {

	private final Long id;

	private final String name;

	private final int memberCount;

	private final int invitationCount;

	public GroupReferenceOutput(final GroupEntity entity) {
		id = entity.getId();
		name = entity.getName();
		// Count the number of members and pending membership invitations
		int tempMemberCount = 0;
		int tempInvitationCount = 0;
		for(final GroupMemberEntity member : entity.getGroupMemberships()) {
			if(member.getMemberType().isMember()) {
				tempMemberCount++;
			} else if(member.getMemberType().isInvitation()) {
				tempInvitationCount++;
			}
		}
		memberCount = tempMemberCount;
		invitationCount = tempInvitationCount;
	}

	@Override
	public int compareTo(final GroupReferenceOutput other) {
		return getName().compareTo(other.getName());
	}
}

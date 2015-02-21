package com.gagror.data.group;

import lombok.Getter;

public class GroupListOutput extends GroupReferenceOutput {

	@Getter
	private final int memberCount;

	@Getter
	private final int invitationCount;

	@Getter
	private final Long memberId;

	public GroupListOutput(final GroupMemberEntity membership) {
		super(membership.getGroup());
		memberId = membership.getId();
		memberCount = countMembers(membership.getGroup(), false);
		invitationCount = countMembers(membership.getGroup(), true);
	}

	public GroupListOutput(final GroupEntity group) {
		super(group);
		memberId = null;
		memberCount = countMembers(group, false);
		invitationCount = countMembers(group, true);
	}

	private static int countMembers(final GroupEntity group, final boolean invitations) {
		// Count the number of members and pending membership invitations
		int tempMemberCount = 0;
		int tempInvitationCount = 0;
		for(final GroupMemberEntity member : group.getGroupMemberships()) {
			if(member.getMemberType().isMember()) {
				tempMemberCount++;
			} else if(member.getMemberType().isInvitation()) {
				tempInvitationCount++;
			}
		}
		if(invitations) {
			return tempInvitationCount;
		} else {
			return tempMemberCount;
		}
	}
}

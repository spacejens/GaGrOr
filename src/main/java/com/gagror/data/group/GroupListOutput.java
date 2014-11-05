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
		super(membership);
		memberId = membership.getId();
		// Count the number of members and pending membership invitations
		int tempMemberCount = 0;
		int tempInvitationCount = 0;
		for(final GroupMemberEntity member : membership.getGroup().getGroupMemberships()) {
			if(member.getMemberType().isMember()) {
				tempMemberCount++;
			} else if(member.getMemberType().isInvitation()) {
				tempInvitationCount++;
			}
		}
		memberCount = tempMemberCount;
		invitationCount = tempInvitationCount;
	}
}

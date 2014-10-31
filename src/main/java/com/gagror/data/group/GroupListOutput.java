package com.gagror.data.group;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class GroupListOutput extends GroupReferenceOutput {

	private final MemberType memberType;

	private final int memberCount;

	private final int invitationCount;

	public GroupListOutput(final GroupMemberEntity entity) {
		super(entity.getGroup());
		memberType = entity.getMemberType();
		// Count the number of members and pending membership invitations
		int tempMemberCount = 0;
		int tempInvitationCount = 0;
		for(final GroupMemberEntity member : entity.getGroup().getGroupMemberships()) {
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

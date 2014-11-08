package com.gagror.data.group;

import lombok.Getter;

import com.gagror.data.account.AccountReferenceOutput;

public class GroupMemberOutput extends AccountReferenceOutput {

	@Getter
	private final MemberType memberType;

	@Getter
	private final boolean onlyOwner;

	public GroupMemberOutput(final GroupMemberEntity member) {
		super(member.getAccount());
		memberType = member.getMemberType();
		onlyOwner = member.getMemberType().isOwner()
				&& 1 == countGroupOwners(member.getGroup());
	}

	private int countGroupOwners(final GroupEntity group) {
		int count = 0;
		for(final GroupMemberEntity member : group.getGroupMemberships()) {
			if(member.getMemberType().isOwner()) {
				count++;
			}
		}
		return count;
	}
}

package com.gagror.data.group;

import lombok.Getter;

import com.gagror.data.account.AccountReferenceOutput;

public class GroupMemberOutput extends AccountReferenceOutput {

	@Getter
	private final MemberType memberType;

	public GroupMemberOutput(final GroupMemberEntity member) {
		super(member.getAccount());
		memberType = member.getMemberType();
	}
}

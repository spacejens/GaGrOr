package com.gagror.data.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.account.AccountReferenceOutput;

public class GroupViewMembersOutput extends GroupReferenceOutput {

	@Getter
	private final List<AccountReferenceOutput> owners;

	@Getter
	private final List<AccountReferenceOutput> members;

	@Getter
	private final List<AccountReferenceOutput> invited;

	public GroupViewMembersOutput(final GroupMemberEntity membership) {
		super(membership);
		owners = extract(membership.getGroup(), MemberType.OWNER);
		members = extract(membership.getGroup(), MemberType.MEMBER);
		invited = extract(membership.getGroup(), MemberType.INVITED);
	}

	private List<AccountReferenceOutput> extract(final GroupEntity group, final MemberType memberType) {
		final List<AccountReferenceOutput> output = new ArrayList<>();
		for(final GroupMemberEntity member : group.getGroupMemberships()) {
			if(memberType.equals(member.getMemberType())) {
				output.add(new AccountReferenceOutput(member.getAccount()));
			}
		}
		Collections.sort(output);
		return Collections.unmodifiableList(output);
	}
}

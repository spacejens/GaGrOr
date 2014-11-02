package com.gagror.data.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.gagror.data.account.AccountReferenceOutput;

@Data
@EqualsAndHashCode(callSuper=true)
public class GroupViewOutput extends GroupReferenceOutput {

	private final Date created;

	private final List<AccountReferenceOutput> owners;

	private final List<AccountReferenceOutput> members;

	private final List<AccountReferenceOutput> invited;

	public GroupViewOutput(final GroupMemberEntity membership) {
		super(membership);
		created = membership.getGroup().getCreated();
		owners = extract(membership.getGroup(), MemberType.OWNER);
		members = extract(membership.getGroup(), MemberType.MEMBER);
		invited = extract(membership.getGroup(), MemberType.INVITED);
	}

	public GroupViewOutput(final GroupEntity group) {
		super(group);
		created = group.getCreated();
		// Not listing members of group when viewing user is not a member
		owners = Collections.emptyList();
		members = Collections.emptyList();
		invited = Collections.emptyList();
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

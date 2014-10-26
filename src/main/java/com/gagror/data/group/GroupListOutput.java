package com.gagror.data.group;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class GroupListOutput extends GroupReferenceOutput {

	private final MemberType memberType;

	public GroupListOutput(final GroupMemberEntity entity) {
		super(entity.getGroup());
		memberType = entity.getMemberType();
	}
}

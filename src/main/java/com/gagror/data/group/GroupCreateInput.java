package com.gagror.data.group;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractNonIdentifiableNamedInput;

@ToString(callSuper=true)
public class GroupCreateInput extends AbstractNonIdentifiableNamedInput<GroupReferenceOutput> {

	@Getter
	@Setter
	private GroupType groupType;
}

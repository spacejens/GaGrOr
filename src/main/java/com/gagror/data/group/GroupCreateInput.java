package com.gagror.data.group;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractInput;

@ToString(callSuper=true)
public class GroupCreateInput extends AbstractInput {

	@Getter
	@Setter
	@Size(min=3, max=64)
	private String name;

	@Getter
	@Setter
	private GroupType groupType;
}

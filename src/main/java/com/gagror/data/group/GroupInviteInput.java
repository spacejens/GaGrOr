package com.gagror.data.group;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractInput;

@ToString(callSuper=true)
public class GroupInviteInput extends AbstractInput {

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private Set<Long> selected;
}

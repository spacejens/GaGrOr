package com.gagror.data.group;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import com.gagror.data.AbstractInput;

public class GroupInviteInput extends AbstractInput {

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private Set<Long> selected;
}

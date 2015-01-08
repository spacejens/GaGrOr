package com.gagror.data.group;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractIdentifiableInput;

@ToString(callSuper=true)
public class GroupInviteInput extends AbstractIdentifiableInput {

	@Getter
	@Setter
	private Set<Long> selected;
}

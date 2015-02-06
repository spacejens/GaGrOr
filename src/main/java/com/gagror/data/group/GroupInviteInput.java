package com.gagror.data.group;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractIdentifiableInput;

@NoArgsConstructor
@ToString(callSuper=true)
public class GroupInviteInput
extends AbstractIdentifiableInput<GroupEditOutput>
implements GroupIdentifiable {

	@Getter
	@Setter
	private Set<Long> selected;

	public GroupInviteInput(final GroupEditOutput currentState) {
		super(currentState);
	}

	@Override
	public Long getGroupId() {
		return getId();
	}
}

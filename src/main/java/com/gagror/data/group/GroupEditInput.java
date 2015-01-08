package com.gagror.data.group;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractIdentifiableNamedInput;

@ToString(callSuper=true)
@NoArgsConstructor
public class GroupEditInput extends AbstractIdentifiableNamedInput<Long, GroupEditOutput> {

	@Getter
	@Setter
	private boolean viewableByAnyone;

	public GroupEditInput(final GroupEditOutput currentState) {
		super(currentState);
		setViewableByAnyone(currentState.isViewableByAnyone());
	}
}

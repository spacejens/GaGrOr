package com.gagror.data.wh40kskirmish;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class Wh40kSkirmishGangTypeInput extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishGangTypeOutput> {

	@Getter
	@Setter
	private Long groupId;

	public Wh40kSkirmishGangTypeInput(final Wh40kSkirmishGangTypeOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
	}

	public Wh40kSkirmishGangTypeInput(final Long groupId) {
		setGroupId(groupId);
	}
}

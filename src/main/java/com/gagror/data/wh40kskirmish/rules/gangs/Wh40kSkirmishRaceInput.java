package com.gagror.data.wh40kskirmish.rules.gangs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class Wh40kSkirmishRaceInput extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishRaceOutput> {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	private Long gangTypeId;

	public Wh40kSkirmishRaceInput(final Wh40kSkirmishRaceOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGangType().getGroup().getId());
		setGangTypeId(currentState.getGangType().getId());
	}

	public Wh40kSkirmishRaceInput(final Long groupId, final Long gangTypeId) {
		setGroupId(groupId);
		setGangTypeId(gangTypeId);
	}
}

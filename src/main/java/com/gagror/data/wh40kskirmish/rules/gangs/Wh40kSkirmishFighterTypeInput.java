package com.gagror.data.wh40kskirmish.rules.gangs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class Wh40kSkirmishFighterTypeInput extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishFighterTypeOutput> {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	private Long gangTypeId;

	@Getter
	@Setter
	private Long raceId;

	public Wh40kSkirmishFighterTypeInput(final Wh40kSkirmishFighterTypeOutput currentState) {
		super(currentState);
		setGroupId(currentState.getRace().getGangType().getGroup().getId());
		setGangTypeId(currentState.getRace().getGangType().getId());
		setRaceId(currentState.getRace().getId());
	}

	public Wh40kSkirmishFighterTypeInput(final Long groupId, final Long gangTypeId, final Long raceId) {
		this.groupId = groupId;
		this.gangTypeId = gangTypeId;
		this.raceId = raceId;
	}
}

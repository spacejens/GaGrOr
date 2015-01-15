package com.gagror.data.wh40kskirmish;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class Wh40kSkirmishFactionInput extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishFactionOutput> {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	private Long gangTypeId;

	public Wh40kSkirmishFactionInput(final Wh40kSkirmishFactionOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
		setGangTypeId(currentState.getGangType().getId());
	}

	public Wh40kSkirmishFactionInput(final Long groupId, final Long gangTypeId) {
		setGroupId(groupId);
		setGangTypeId(gangTypeId);
	}
}

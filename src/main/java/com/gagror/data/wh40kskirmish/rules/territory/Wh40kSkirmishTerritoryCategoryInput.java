package com.gagror.data.wh40kskirmish.rules.territory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class Wh40kSkirmishTerritoryCategoryInput
extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishTerritoryCategoryOutput> {

	@Getter
	@Setter
	private Long groupId;

	public Wh40kSkirmishTerritoryCategoryInput(final Wh40kSkirmishTerritoryCategoryOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
	}

	public Wh40kSkirmishTerritoryCategoryInput(final Long groupId) {
		setGroupId(groupId);
	}
}

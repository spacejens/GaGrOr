package com.gagror.data.wh40kskirmish.rules;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableInput;

@NoArgsConstructor
public class Wh40kSkirmishRulesInput
extends AbstractIdentifiableInput<Long, Wh40kSkirmishRulesOutput> {

	@Getter
	@Setter
	private Long groupId;

	public Wh40kSkirmishRulesInput(final Wh40kSkirmishRulesOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
	}

	public Wh40kSkirmishRulesInput(final Long groupId) {
		setGroupId(groupId);
	}
}

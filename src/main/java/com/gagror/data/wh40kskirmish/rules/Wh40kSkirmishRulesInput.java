package com.gagror.data.wh40kskirmish.rules;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

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

	@Getter
	@Setter
	@Min(1)
	@Max(10000)
	private int startingMoney;

	@Getter
	@Setter
	@Size(min=1, max=16)
	private String currencyName;

	public Wh40kSkirmishRulesInput(final Wh40kSkirmishRulesOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
		setStartingMoney(currentState.getStartingMoney());
		setCurrencyName(currentState.getCurrencyName());
	}
}

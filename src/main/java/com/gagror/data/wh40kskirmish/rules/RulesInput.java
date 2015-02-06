package com.gagror.data.wh40kskirmish.rules;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableInput;
import com.gagror.data.group.GroupIdentifiable;

@NoArgsConstructor
public class RulesInput
extends AbstractIdentifiableInput<RulesOutput>
implements GroupIdentifiable {

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

	public RulesInput(final RulesOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
		setStartingMoney(currentState.getStartingMoney());
		setCurrencyName(currentState.getCurrencyName());
	}
}

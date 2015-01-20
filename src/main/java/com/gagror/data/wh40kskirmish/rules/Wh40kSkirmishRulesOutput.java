package com.gagror.data.wh40kskirmish.rules;

import lombok.Getter;

import com.gagror.data.AbstractEditableEntityOutput;
import com.gagror.data.group.GroupReferenceOutput;

public class Wh40kSkirmishRulesOutput
extends AbstractEditableEntityOutput {

	@Getter
	private final GroupReferenceOutput group;

	@Getter
	private final int startingMoney;

	@Getter
	private final String currencyName;

	public Wh40kSkirmishRulesOutput(final Wh40kSkirmishRulesEntity entity, final GroupReferenceOutput group) {
		super(entity);
		this.group = group;
		this.startingMoney = entity.getStartingMoney();
		this.currencyName = entity.getCurrencyName();
	}
}

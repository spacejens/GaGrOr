package com.gagror.data.wh40kskirmish.rules;

import lombok.Getter;

import com.gagror.data.AbstractEditableEntityOutput;
import com.gagror.data.group.GroupOwnedOutput;
import com.gagror.data.group.GroupReferenceOutput;

public class RulesOutput
extends AbstractEditableEntityOutput
implements GroupOwnedOutput {

	@Getter
	private final GroupReferenceOutput group;

	@Getter
	private final int startingMoney;

	@Getter
	private final String currencyName;

	public RulesOutput(final Wh40kSkirmishRulesEntity entity) {
		super(entity);
		this.group = new GroupReferenceOutput(entity.getGroup());
		this.startingMoney = entity.getStartingMoney();
		this.currencyName = entity.getCurrencyName();
	}
}

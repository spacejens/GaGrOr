package com.gagror.data.wh40kskirmish.gangs;

import lombok.Getter;

import com.gagror.data.wh40kskirmish.rules.RulesOutput;

public class GangOutput extends GangListOutput {

	@Getter
	private final RulesOutput rules;

	@Getter
	private final int money;

	public GangOutput(final GangEntity entity, final RulesOutput rules) {
		super(entity);
		this.rules = rules;
		money = entity.getMoney();
	}
}

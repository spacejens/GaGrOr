package com.gagror.data.wh40kskirmish.gangs;

import lombok.Getter;

import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesOutput;

public class Wh40kSkirmishGangOutput extends Wh40kSkirmishGangListOutput {

	@Getter
	private final Wh40kSkirmishRulesOutput rules;

	@Getter
	private final int money;

	public Wh40kSkirmishGangOutput(final Wh40kSkirmishGangEntity entity, final Wh40kSkirmishRulesOutput rules) {
		super(entity);
		this.rules = rules;
		money = entity.getMoney();
	}
}

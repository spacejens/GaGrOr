package com.gagror.data.wh40kskirmish.gangs;

import lombok.Getter;

import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeReferenceOutput;

public class GangOutput extends GangListOutput {

	@Getter
	private final RulesOutput rules;

	@Getter
	private final GangTypeReferenceOutput gangType;

	@Getter
	private final FactionReferenceOutput faction;

	@Getter
	private final AccountReferenceOutput player;

	@Getter
	private final int money;

	public GangOutput(final GangEntity entity, final RulesOutput rules) {
		super(entity);
		this.rules = rules;
		gangType = new GangTypeReferenceOutput(entity.getFaction().getGangType());
		faction = new FactionReferenceOutput(entity.getFaction());
		player = new AccountReferenceOutput(entity.getPlayer());
		money = entity.getMoney();
	}
}

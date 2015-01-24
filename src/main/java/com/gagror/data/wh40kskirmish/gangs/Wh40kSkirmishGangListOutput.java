package com.gagror.data.wh40kskirmish.gangs;

import lombok.Getter;

import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFactionReferenceOutput;

public class Wh40kSkirmishGangListOutput extends Wh40kSkirmishGangReferenceOutput {

	@Getter
	private final Wh40kSkirmishFactionReferenceOutput faction;

	@Getter
	private final AccountReferenceOutput player;

	public Wh40kSkirmishGangListOutput(final Wh40kSkirmishGangEntity entity) {
		super(entity);
		faction = new Wh40kSkirmishFactionReferenceOutput(entity.getFaction());
		player = new AccountReferenceOutput(entity.getPlayer());
	}
}

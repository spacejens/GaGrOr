package com.gagror.data.wh40kskirmish.gangs;

import lombok.Getter;

import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeReferenceOutput;

public class GangListOutput extends GangReferenceOutput {

	@Getter
	private final FactionReferenceOutput faction;

	@Getter
	private final GangTypeReferenceOutput gangType;

	@Getter
	private final AccountReferenceOutput player;

	public GangListOutput(final GangEntity entity) {
		super(entity);
		faction = new FactionReferenceOutput(entity.getFaction());
		gangType = new GangTypeReferenceOutput(entity.getFaction().getGangType());
		player = new AccountReferenceOutput(entity.getPlayer());
	}
}

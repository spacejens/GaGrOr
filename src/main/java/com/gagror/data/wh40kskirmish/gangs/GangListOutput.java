package com.gagror.data.wh40kskirmish.gangs;

import lombok.Getter;

import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.group.PlayerOwnedOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeReferenceOutput;

public class GangListOutput extends GangReferenceOutput implements PlayerOwnedOutput {

	@Getter
	private final FactionReferenceOutput faction;

	@Getter
	private final GangTypeReferenceOutput gangType;

	@Getter
	private final GroupReferenceOutput group;

	@Getter
	private final AccountReferenceOutput player;

	public GangListOutput(final GangEntity entity) {
		super(entity);
		faction = new FactionReferenceOutput(entity.getFaction());
		gangType = new GangTypeReferenceOutput(entity.getFaction().getGangType());
		group = new GroupReferenceOutput(entity.getGroup());
		player = new AccountReferenceOutput(entity.getPlayer());
	}
}

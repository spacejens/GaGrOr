package com.gagror.data.wh40kskirmish.rules.gangs;

import lombok.Getter;

public class FactionOutput extends FactionReferenceOutput {

	@Getter
	private final GangTypeOutput gangType;

	public FactionOutput(
			final FactionEntity entity,
			final GangTypeOutput gangType) {
		super(entity);
		this.gangType = gangType;
	}
}

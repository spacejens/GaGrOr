package com.gagror.data.wh40kskirmish.gangs;

import lombok.Getter;

public class FighterEditOutput extends FighterReferenceOutput {

	@Getter
	private final GangReferenceOutput gang;

	public FighterEditOutput(final FighterEntity entity) {
		super(entity);
		gang = new GangReferenceOutput(entity.getGang());
	}
}

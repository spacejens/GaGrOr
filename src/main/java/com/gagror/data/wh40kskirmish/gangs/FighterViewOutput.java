package com.gagror.data.wh40kskirmish.gangs;

import lombok.RequiredArgsConstructor;

public class FighterViewOutput extends FighterReferenceOutput {

	protected FighterViewOutput(final FighterEntity entity) {
		super(entity);
	}

	@RequiredArgsConstructor
	public static class Builder {
		final FighterEntity entity;

		public FighterViewOutput build() {
			return new FighterViewOutput(entity);
		}
	}
}

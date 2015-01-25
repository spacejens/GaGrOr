package com.gagror.data.wh40kskirmish.rules.gangs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

public class RaceListChildrenOutput
extends RaceReferenceOutput {

	@Getter
	private final List<FighterTypeReferenceOutput> fighterTypes;

	public RaceListChildrenOutput(final RaceEntity entity) {
		super(entity);
		// Extract a sorted list of fighter types
		final List<FighterTypeReferenceOutput> fighterTypes = new ArrayList<>();
		for(final FighterTypeEntity fighterType : entity.getFighterTypes()) {
			fighterTypes.add(new FighterTypeReferenceOutput(fighterType));
		}
		Collections.sort(fighterTypes);
		this.fighterTypes = fighterTypes;
	}
}

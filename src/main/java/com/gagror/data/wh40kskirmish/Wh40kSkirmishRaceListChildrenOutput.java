package com.gagror.data.wh40kskirmish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

public class Wh40kSkirmishRaceListChildrenOutput
extends Wh40kSkirmishRaceReferenceOutput {

	@Getter
	private final List<Wh40kSkirmishFighterTypeReferenceOutput> fighterTypes;

	public Wh40kSkirmishRaceListChildrenOutput(final Wh40kSkirmishRaceEntity entity) {
		super(entity);
		// Extract a sorted list of fighter types
		final List<Wh40kSkirmishFighterTypeReferenceOutput> fighterTypes = new ArrayList<>();
		for(final Wh40kSkirmishFighterTypeEntity fighterType : entity.getFighterTypes()) {
			fighterTypes.add(new Wh40kSkirmishFighterTypeReferenceOutput(fighterType));
		}
		Collections.sort(fighterTypes);
		this.fighterTypes = fighterTypes;
	}
}

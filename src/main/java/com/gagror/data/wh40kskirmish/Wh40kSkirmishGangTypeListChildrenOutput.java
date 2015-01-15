package com.gagror.data.wh40kskirmish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

public class Wh40kSkirmishGangTypeListChildrenOutput extends Wh40kSkirmishGangTypeReferenceOutput {

	@Getter
	private final List<Wh40kSkirmishFactionReferenceOutput> factions;

	@Getter
	private final List<Wh40kSkirmishRaceListChildrenOutput> races;

	public Wh40kSkirmishGangTypeListChildrenOutput(final Wh40kSkirmishGangTypeEntity entity) {
		super(entity);
		// Extract a sorted list of factions
		final List<Wh40kSkirmishFactionReferenceOutput> factions = new ArrayList<>();
		for(final Wh40kSkirmishFactionEntity faction : entity.getFactions()) {
			factions.add(new Wh40kSkirmishFactionReferenceOutput(faction));
		}
		Collections.sort(factions);
		this.factions = factions;
		// Extract a sorted list of races
		final List<Wh40kSkirmishRaceListChildrenOutput> races = new ArrayList<>();
		for(final Wh40kSkirmishRaceEntity race : entity.getRaces()) {
			races.add(new Wh40kSkirmishRaceListChildrenOutput(race));
		}
		Collections.sort(races);
		this.races = races;
	}
}

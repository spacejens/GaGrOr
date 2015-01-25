package com.gagror.data.wh40kskirmish.rules.gangs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

public class GangTypeListChildrenOutput extends GangTypeReferenceOutput {

	@Getter
	private final List<FactionReferenceOutput> factions;

	@Getter
	private final List<RaceListChildrenOutput> races;

	public GangTypeListChildrenOutput(final GangTypeEntity entity) {
		super(entity);
		// Extract a sorted list of factions
		final List<FactionReferenceOutput> factions = new ArrayList<>();
		for(final FactionEntity faction : entity.getFactions()) {
			factions.add(new FactionReferenceOutput(faction));
		}
		Collections.sort(factions);
		this.factions = factions;
		// Extract a sorted list of races
		final List<RaceListChildrenOutput> races = new ArrayList<>();
		for(final RaceEntity race : entity.getRaces()) {
			races.add(new RaceListChildrenOutput(race));
		}
		Collections.sort(races);
		this.races = races;
	}
}

package com.gagror.data.wh40kskirmish.rules.territory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

public class Wh40kSkirmishTerritoryCategoryListChildrenOutput
extends Wh40kSkirmishTerritoryCategoryReferenceOutput {

	@Getter
	private final List<Wh40kSkirmishTerritoryTypeReferenceOutput> territoryTypes;

	public Wh40kSkirmishTerritoryCategoryListChildrenOutput(final Wh40kSkirmishTerritoryCategoryEntity entity) {
		super(entity);
		// Sorted list of territory types
		final List<Wh40kSkirmishTerritoryTypeReferenceOutput> tempTerritoryTypes = new ArrayList<>();
		for(final Wh40kSkirmishTerritoryTypeEntity territoryType : entity.getTerritoryTypes()) {
			tempTerritoryTypes.add(new Wh40kSkirmishTerritoryTypeReferenceOutput(territoryType));
		}
		Collections.sort(tempTerritoryTypes);
		territoryTypes = Collections.unmodifiableList(tempTerritoryTypes);
	}
}

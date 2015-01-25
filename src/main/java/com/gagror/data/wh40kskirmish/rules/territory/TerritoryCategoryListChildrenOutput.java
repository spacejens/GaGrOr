package com.gagror.data.wh40kskirmish.rules.territory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

public class TerritoryCategoryListChildrenOutput
extends TerritoryCategoryReferenceOutput {

	@Getter
	private final List<TerritoryTypeReferenceOutput> territoryTypes;

	public TerritoryCategoryListChildrenOutput(final TerritoryCategoryEntity entity) {
		super(entity);
		// Sorted list of territory types
		final List<TerritoryTypeReferenceOutput> tempTerritoryTypes = new ArrayList<>();
		for(final TerritoryTypeEntity territoryType : entity.getTerritoryTypes()) {
			tempTerritoryTypes.add(new TerritoryTypeReferenceOutput(territoryType));
		}
		Collections.sort(tempTerritoryTypes);
		territoryTypes = Collections.unmodifiableList(tempTerritoryTypes);
	}
}

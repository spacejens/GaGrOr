package com.gagror.data.wh40kskirmish.rules.territory;

import lombok.Getter;

public class TerritoryTypeOutput extends TerritoryTypeReferenceOutput {

	@Getter
	private final TerritoryCategoryOutput territoryCategory;

	public TerritoryTypeOutput(
			final TerritoryTypeEntity entity,
			final TerritoryCategoryOutput territoryCategory) {
		super(entity);
		this.territoryCategory = territoryCategory;
	}
}

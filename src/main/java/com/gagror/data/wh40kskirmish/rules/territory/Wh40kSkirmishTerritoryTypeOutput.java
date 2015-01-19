package com.gagror.data.wh40kskirmish.rules.territory;

import lombok.Getter;

public class Wh40kSkirmishTerritoryTypeOutput extends Wh40kSkirmishTerritoryTypeReferenceOutput {

	@Getter
	private final Wh40kSkirmishTerritoryCategoryOutput territoryCategory;

	public Wh40kSkirmishTerritoryTypeOutput(
			final Wh40kSkirmishTerritoryTypeEntity entity,
			final Wh40kSkirmishTerritoryCategoryOutput territoryCategory) {
		super(entity);
		this.territoryCategory = territoryCategory;
	}
}

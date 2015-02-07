package com.gagror.data.wh40kskirmish.rules.territory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TerritoryTypeRepository {

	@Autowired
	TerritoryTypeRepositoryQueries territoryTypeRepositoryQueries;

	public TerritoryTypeEntity persist(final TerritoryTypeEntity territoryType) {
		return territoryTypeRepositoryQueries.save(territoryType);
	}
}

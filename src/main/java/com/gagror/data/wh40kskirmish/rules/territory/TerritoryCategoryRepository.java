package com.gagror.data.wh40kskirmish.rules.territory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TerritoryCategoryRepository {

	@Autowired
	TerritoryCategoryRepositoryQueries territoryCategoryRepositoryQueries;

	public TerritoryCategoryEntity persist(final TerritoryCategoryEntity territoryCategory) {
		return territoryCategoryRepositoryQueries.save(territoryCategory);
	}
}

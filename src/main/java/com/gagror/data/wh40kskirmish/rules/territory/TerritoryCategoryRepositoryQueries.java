package com.gagror.data.wh40kskirmish.rules.territory;

import org.springframework.data.repository.Repository;

interface TerritoryCategoryRepositoryQueries extends Repository<TerritoryCategoryEntity, Long> {

	TerritoryCategoryEntity findOne(final Long territoryCategoryId);

	TerritoryCategoryEntity save(final TerritoryCategoryEntity territoryCategory);
}

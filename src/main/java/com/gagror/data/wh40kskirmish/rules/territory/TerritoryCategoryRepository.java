package com.gagror.data.wh40kskirmish.rules.territory;

import org.springframework.data.repository.Repository;

public interface TerritoryCategoryRepository extends Repository<TerritoryCategoryEntity, Long> {

	TerritoryCategoryEntity save(final TerritoryCategoryEntity gangType);
}

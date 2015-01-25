package com.gagror.data.wh40kskirmish.rules.territory;

import org.springframework.data.repository.Repository;

public interface TerritoryTypeRepository extends Repository<TerritoryTypeEntity, Long> {

	TerritoryTypeEntity save(final TerritoryTypeEntity gangType);
}

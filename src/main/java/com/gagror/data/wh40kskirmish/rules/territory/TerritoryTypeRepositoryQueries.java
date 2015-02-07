package com.gagror.data.wh40kskirmish.rules.territory;

import org.springframework.data.repository.Repository;

interface TerritoryTypeRepositoryQueries extends Repository<TerritoryTypeEntity, Long> {

	TerritoryTypeEntity findOne(final Long territoryTypeId);

	TerritoryTypeEntity save(final TerritoryTypeEntity territoryType);
}

package com.gagror.data.wh40kskirmish.rules.territory;

import org.springframework.data.repository.Repository;

public interface Wh40kSkirmishTerritoryTypeRepository extends Repository<Wh40kSkirmishTerritoryTypeEntity, Long> {

	Wh40kSkirmishTerritoryTypeEntity save(final Wh40kSkirmishTerritoryTypeEntity gangType);
}

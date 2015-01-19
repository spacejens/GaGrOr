package com.gagror.data.wh40kskirmish.rules.territory;

import org.springframework.data.repository.Repository;

public interface Wh40kSkirmishTerritoryCategoryRepository extends Repository<Wh40kSkirmishTerritoryCategoryEntity, Long> {

	Wh40kSkirmishTerritoryCategoryEntity save(final Wh40kSkirmishTerritoryCategoryEntity gangType);
}

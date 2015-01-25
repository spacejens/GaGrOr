package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.data.repository.Repository;

public interface RaceRepository extends Repository<RaceEntity, Long> {

	RaceEntity save(final RaceEntity race);
}

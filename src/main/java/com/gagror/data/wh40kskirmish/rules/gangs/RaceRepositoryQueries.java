package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.data.repository.Repository;

interface RaceRepositoryQueries extends Repository<RaceEntity, Long> {

	RaceEntity save(final RaceEntity race);
}

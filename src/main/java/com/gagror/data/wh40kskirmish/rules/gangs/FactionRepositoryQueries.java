package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.data.repository.Repository;

interface FactionRepositoryQueries extends Repository<FactionEntity, Long> {

	FactionEntity save(final FactionEntity faction);
}

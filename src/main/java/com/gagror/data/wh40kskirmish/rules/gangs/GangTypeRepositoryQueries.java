package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.data.repository.Repository;

interface GangTypeRepositoryQueries extends Repository<GangTypeEntity, Long> {

	GangTypeEntity save(final GangTypeEntity gangType);
}

package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.data.repository.Repository;

interface GangTypeRepositoryQueries extends Repository<GangTypeEntity, Long> {

	GangTypeEntity findOne(final Long gangTypeId);

	GangTypeEntity save(final GangTypeEntity gangType);
}

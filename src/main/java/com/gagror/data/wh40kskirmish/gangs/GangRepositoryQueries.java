package com.gagror.data.wh40kskirmish.gangs;

import org.springframework.data.repository.Repository;

interface GangRepositoryQueries extends Repository<GangEntity, Long> {

	GangEntity findOne(final Long gangId);

	GangEntity save(final GangEntity gang);
}

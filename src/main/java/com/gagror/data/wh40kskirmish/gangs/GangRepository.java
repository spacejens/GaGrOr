package com.gagror.data.wh40kskirmish.gangs;

import org.springframework.data.repository.Repository;

public interface GangRepository extends Repository<GangEntity, Long> {

	GangEntity save(final GangEntity gang);
}

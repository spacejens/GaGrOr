package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.data.repository.Repository;

public interface FactionRepository extends Repository<FactionEntity, Long> {

	FactionEntity save(final FactionEntity faction);
}

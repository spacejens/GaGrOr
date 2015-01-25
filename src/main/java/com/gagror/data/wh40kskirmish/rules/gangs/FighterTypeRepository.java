package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.data.repository.Repository;

public interface FighterTypeRepository extends Repository<FighterTypeEntity, Long> {

	FighterTypeEntity save(final FighterTypeEntity race);
}

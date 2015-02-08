package com.gagror.data.wh40kskirmish.gangs;

import org.springframework.data.repository.Repository;

interface FighterRepositoryQueries extends Repository<FighterEntity, Long> {

	FighterEntity findOne(final Long fighterId);

	FighterEntity save(final FighterEntity fighter);
}

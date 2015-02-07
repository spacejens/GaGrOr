package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.data.repository.Repository;

interface FighterTypeRepositoryQueries extends Repository<FighterTypeEntity, Long> {

	FighterTypeEntity findOne(final Long fighterTypeId);

	FighterTypeEntity save(final FighterTypeEntity fighterType);
}

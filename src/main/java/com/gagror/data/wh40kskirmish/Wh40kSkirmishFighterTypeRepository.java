package com.gagror.data.wh40kskirmish;

import org.springframework.data.repository.Repository;

public interface Wh40kSkirmishFighterTypeRepository extends Repository<Wh40kSkirmishFighterTypeEntity, Long> {

	Wh40kSkirmishFighterTypeEntity save(final Wh40kSkirmishFighterTypeEntity race);
}

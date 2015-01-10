package com.gagror.data.wh40kskirmish;

import org.springframework.data.repository.Repository;

public interface Wh40kSkirmishGangTypeRepository extends Repository<Wh40kSkirmishGangTypeEntity, Long> {

	Wh40kSkirmishGangTypeEntity save(final Wh40kSkirmishGangTypeEntity gangType);
}

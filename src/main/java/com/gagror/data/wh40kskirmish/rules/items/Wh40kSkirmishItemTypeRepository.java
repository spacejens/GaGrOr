package com.gagror.data.wh40kskirmish.rules.items;

import org.springframework.data.repository.Repository;

public interface Wh40kSkirmishItemTypeRepository extends Repository<Wh40kSkirmishItemTypeEntity, Long> {

	Wh40kSkirmishItemTypeEntity save(final Wh40kSkirmishItemTypeEntity gangType);
}

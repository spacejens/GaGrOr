package com.gagror.data.wh40kskirmish.rules.items;

import org.springframework.data.repository.Repository;

public interface Wh40kSkirmishItemCategoryRepository extends Repository<Wh40kSkirmishItemCategoryEntity, Long> {

	Wh40kSkirmishItemCategoryEntity save(final Wh40kSkirmishItemCategoryEntity gangType);
}

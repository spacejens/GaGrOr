package com.gagror.data.wh40kskirmish.rules.items;

import org.springframework.data.repository.Repository;

public interface ItemTypeRepository extends Repository<ItemTypeEntity, Long> {

	ItemTypeEntity save(final ItemTypeEntity gangType);
}

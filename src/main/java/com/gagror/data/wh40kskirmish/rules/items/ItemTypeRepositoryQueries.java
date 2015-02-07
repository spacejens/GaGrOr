package com.gagror.data.wh40kskirmish.rules.items;

import org.springframework.data.repository.Repository;

interface ItemTypeRepositoryQueries extends Repository<ItemTypeEntity, Long> {

	ItemTypeEntity findOne(final Long itemTypeId);

	ItemTypeEntity save(final ItemTypeEntity itemType);
}

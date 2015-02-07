package com.gagror.data.wh40kskirmish.rules.items;

import org.springframework.data.repository.Repository;

interface ItemCategoryRepositoryQueries extends Repository<ItemCategoryEntity, Long> {

	ItemCategoryEntity findOne(final Long itemCategoryId);

	ItemCategoryEntity save(final ItemCategoryEntity itemCategory);
}

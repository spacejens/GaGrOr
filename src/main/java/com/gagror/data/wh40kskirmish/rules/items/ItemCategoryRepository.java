package com.gagror.data.wh40kskirmish.rules.items;

import org.springframework.data.repository.Repository;

public interface ItemCategoryRepository extends Repository<ItemCategoryEntity, Long> {

	ItemCategoryEntity save(final ItemCategoryEntity gangType);
}

package com.gagror.data.wh40kskirmish.rules.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ItemCategoryRepository {

	@Autowired
	ItemCategoryRepositoryQueries itemCategoryRepositoryQueries;

	public ItemCategoryEntity persist(final ItemCategoryEntity itemCategory) {
		return itemCategoryRepositoryQueries.save(itemCategory);
	}
}

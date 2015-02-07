package com.gagror.data.wh40kskirmish.rules.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ItemTypeRepository {

	@Autowired
	ItemTypeRepositoryQueries itemTypeRepositoryQueries;

	public ItemTypeEntity persist(final ItemTypeEntity itemType) {
		return itemTypeRepositoryQueries.save(itemType);
	}
}

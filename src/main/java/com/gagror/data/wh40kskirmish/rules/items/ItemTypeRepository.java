package com.gagror.data.wh40kskirmish.rules.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class ItemTypeRepository {

	@Autowired
	ItemCategoryRepository itemCategoryRepository;

	@Autowired
	ItemTypeRepositoryQueries itemTypeRepositoryQueries;

	public ItemTypeEntity persist(final ItemTypeEntity itemType) {
		return itemTypeRepositoryQueries.save(itemType);
	}

	public ItemTypeEntity load(final Long groupId, final Long itemCategoryId, final Long itemTypeId) {
		// TODO Load item type from ID using query, verify group after loading. Category ID no longer needed
		final ItemCategoryEntity itemCategory = itemCategoryRepository.load(groupId, itemCategoryId);
		for(final ItemTypeEntity itemType : itemCategory.getItemTypes()) {
			if(itemType.hasId(itemTypeId)) {
				return itemType;
			}
		}
		throw new DataNotFoundException(String.format("Item type %d (item category %d, group %d)",
				itemTypeId, itemCategoryId, groupId));
	}
}

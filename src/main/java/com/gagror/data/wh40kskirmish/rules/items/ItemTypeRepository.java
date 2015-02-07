package com.gagror.data.wh40kskirmish.rules.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class ItemTypeRepository {

	@Autowired
	ItemTypeRepositoryQueries itemTypeRepositoryQueries;

	public ItemTypeEntity persist(final ItemTypeEntity itemType) {
		return itemTypeRepositoryQueries.save(itemType);
	}

	public ItemTypeEntity load(final Long groupId, final Long itemTypeId) {
		final ItemTypeEntity itemType = itemTypeRepositoryQueries.findOne(itemTypeId);
		if(null != itemType && itemType.getGroup().hasId(groupId)) {
			return itemType;
		}
		throw new DataNotFoundException(String.format("Item type %d (group %d)",
				itemTypeId, groupId));
	}
}

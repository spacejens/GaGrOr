package com.gagror.data.wh40kskirmish.rules.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;

@Repository
public class ItemCategoryRepository {

	@Autowired
	RulesRepository rulesRepository;

	@Autowired
	ItemCategoryRepositoryQueries itemCategoryRepositoryQueries;

	public ItemCategoryEntity persist(final ItemCategoryEntity itemCategory) {
		return itemCategoryRepositoryQueries.save(itemCategory);
	}

	public ItemCategoryEntity load(final Long groupId, final Long itemCategoryId) {
		final ItemCategoryEntity itemCategory = itemCategoryRepositoryQueries.findOne(itemCategoryId);
		if(null != itemCategory && itemCategory.getGroup().hasId(groupId)) {
			return itemCategory;
		}
		throw new DataNotFoundException(String.format("Item category %d (group %d)", itemCategoryId, groupId));
	}
}

package com.gagror.data.wh40kskirmish.rules.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;

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
		// TODO Load item category from ID using query, verify group after loading
		final Wh40kSkirmishRulesEntity rules = rulesRepository.load(groupId);
		for(final ItemCategoryEntity itemCategory : rules.getItemCategories()) {
			if(itemCategory.hasId(itemCategoryId)) {
				return itemCategory;
			}
		}
		throw new DataNotFoundException(String.format("Item category %d (group %d)", itemCategoryId, groupId));
	}
}

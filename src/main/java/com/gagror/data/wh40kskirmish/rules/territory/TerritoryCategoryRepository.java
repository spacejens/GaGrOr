package com.gagror.data.wh40kskirmish.rules.territory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;

@Repository
public class TerritoryCategoryRepository {

	@Autowired
	RulesRepository rulesRepository;

	@Autowired
	TerritoryCategoryRepositoryQueries territoryCategoryRepositoryQueries;

	public TerritoryCategoryEntity persist(final TerritoryCategoryEntity territoryCategory) {
		return territoryCategoryRepositoryQueries.save(territoryCategory);
	}

	public TerritoryCategoryEntity load(final Long groupId, final Long territoryCategoryId) {
		// TODO Load territory category from ID using query, verify group after loading
		final Wh40kSkirmishRulesEntity rules = rulesRepository.load(groupId);
		for(final TerritoryCategoryEntity territoryCategory : rules.getTerritoryCategories()) {
			if(territoryCategory.hasId(territoryCategoryId)) {
				return territoryCategory;
			}
		}
		throw new DataNotFoundException(String.format("Territory category %d (group %d)", territoryCategoryId, groupId));
	}
}

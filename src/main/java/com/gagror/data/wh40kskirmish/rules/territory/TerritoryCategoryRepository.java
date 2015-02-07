package com.gagror.data.wh40kskirmish.rules.territory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;

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
		final TerritoryCategoryEntity territoryCategory = territoryCategoryRepositoryQueries.findOne(territoryCategoryId);
		if(null != territoryCategory && territoryCategory.getGroup().hasId(groupId)) {
			return territoryCategory;
		}
		throw new DataNotFoundException(String.format("Territory category %d (group %d)", territoryCategoryId, groupId));
	}
}

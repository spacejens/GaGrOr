package com.gagror.data.wh40kskirmish.rules.territory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class TerritoryTypeRepository {

	@Autowired
	TerritoryCategoryRepository territoryCategoryRepository;

	@Autowired
	TerritoryTypeRepositoryQueries territoryTypeRepositoryQueries;

	public TerritoryTypeEntity persist(final TerritoryTypeEntity territoryType) {
		return territoryTypeRepositoryQueries.save(territoryType);
	}

	public TerritoryTypeEntity load(final Long groupId, final Long territoryCategoryId, final Long territoryTypeId) {
		// TODO Load territory type from ID using query, verify group after loading. Category ID no longer needed
		final TerritoryCategoryEntity territoryCategory = territoryCategoryRepository.load(groupId, territoryCategoryId);
		for(final TerritoryTypeEntity territoryType : territoryCategory.getTerritoryTypes()) {
			if(territoryType.hasId(territoryTypeId)) {
				return territoryType;
			}
		}
		throw new DataNotFoundException(String.format("Territory type %d (territory category %d, group %d)",
				territoryTypeId, territoryCategoryId, groupId));
	}
}

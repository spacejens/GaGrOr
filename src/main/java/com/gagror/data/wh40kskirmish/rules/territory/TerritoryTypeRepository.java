package com.gagror.data.wh40kskirmish.rules.territory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class TerritoryTypeRepository {

	@Autowired
	TerritoryTypeRepositoryQueries territoryTypeRepositoryQueries;

	public TerritoryTypeEntity persist(final TerritoryTypeEntity territoryType) {
		return territoryTypeRepositoryQueries.save(territoryType);
	}

	public TerritoryTypeEntity load(final Long groupId, final Long territoryTypeId) {
		final TerritoryTypeEntity territoryType = territoryTypeRepositoryQueries.findOne(territoryTypeId);
		if(null != territoryType && territoryType.getGroup().hasId(groupId)) {
			return territoryType;
		}
		throw new DataNotFoundException(String.format("Territory type %d (group %d)",
				territoryTypeId, groupId));
	}
}

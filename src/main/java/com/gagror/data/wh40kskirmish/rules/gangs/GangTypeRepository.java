package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;

@Repository
public class GangTypeRepository {

	@Autowired
	RulesRepository rulesRepository;

	@Autowired
	GangTypeRepositoryQueries gangTypeRepositoryQueries;

	public GangTypeEntity persist(final GangTypeEntity gangType) {
		return gangTypeRepositoryQueries.save(gangType);
	}

	public GangTypeEntity load(final Long groupId, final Long gangTypeId) {
		final GangTypeEntity gangType = gangTypeRepositoryQueries.findOne(gangTypeId);
		if(null != gangType && gangType.getGroup().hasId(groupId)) {
			return gangType;
		}
		throw new DataNotFoundException(String.format("Gang type %d (group %d)", gangTypeId, groupId));
	}
}

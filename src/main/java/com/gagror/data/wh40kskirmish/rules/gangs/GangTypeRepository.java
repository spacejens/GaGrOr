package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GangTypeRepository {

	@Autowired
	GangTypeRepositoryQueries gangTypeRepositoryQueries;

	public GangTypeEntity persist(final GangTypeEntity gangType) {
		return gangTypeRepositoryQueries.save(gangType);
	}
}

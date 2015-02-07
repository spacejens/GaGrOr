package com.gagror.data.wh40kskirmish.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GangRepository {

	@Autowired
	GangRepositoryQueries gangRepositoryQueries;

	public GangEntity persist(final GangEntity gang) {
		return gangRepositoryQueries.save(gang);
	}
}

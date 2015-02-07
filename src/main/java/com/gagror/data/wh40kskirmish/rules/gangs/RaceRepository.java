package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RaceRepository {

	@Autowired
	RaceRepositoryQueries raceRepositoryQueries;

	public RaceEntity persist(final RaceEntity race) {
		return raceRepositoryQueries.save(race);
	}
}

package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class RaceRepository {

	@Autowired
	RaceRepositoryQueries raceRepositoryQueries;

	public RaceEntity persist(final RaceEntity race) {
		return raceRepositoryQueries.save(race);
	}

	public RaceEntity load(final Long groupId, final Long raceId) {
		final RaceEntity race = raceRepositoryQueries.findOne(raceId);
		if(null != race && race.getGroup().hasId(groupId)) {
			return race;
		}
		throw new DataNotFoundException(String.format("Race %d (group %d)",
				raceId, groupId));
	}
}

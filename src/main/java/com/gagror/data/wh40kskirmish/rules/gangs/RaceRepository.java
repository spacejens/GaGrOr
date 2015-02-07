package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class RaceRepository {

	@Autowired
	GangTypeRepository gangTypeRepository;

	@Autowired
	RaceRepositoryQueries raceRepositoryQueries;

	public RaceEntity persist(final RaceEntity race) {
		return raceRepositoryQueries.save(race);
	}

	public RaceEntity load(final Long groupId, final Long gangTypeId, final Long raceId) {
		// TODO Load race from ID using query, verify group after loading. Gang type ID no longer needed
		final GangTypeEntity gangType = gangTypeRepository.load(groupId, gangTypeId);
		for(final RaceEntity race : gangType.getRaces()) {
			if(race.hasId(raceId)) {
				return race;
			}
		}
		throw new DataNotFoundException(String.format("Race %d (gang type %d, group %d)",
				raceId, gangTypeId, groupId));
	}
}

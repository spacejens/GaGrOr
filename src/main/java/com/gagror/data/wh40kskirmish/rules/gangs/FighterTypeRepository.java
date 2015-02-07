package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class FighterTypeRepository {

	@Autowired
	RaceRepository raceRepository;

	@Autowired
	FighterTypeRepositoryQueries fighterTypeRepositoryQueries;

	public FighterTypeEntity persist(final FighterTypeEntity fighterType) {
		return fighterTypeRepositoryQueries.save(fighterType);
	}

	public FighterTypeEntity load(
			final Long groupId,
			final Long gangTypeId,
			final Long raceId,
			final Long fighterTypeId) {
		// TODO Load fighter type from ID using query, verify group after loading. Gang type and race IDs no longer needed
		final RaceEntity race = raceRepository.load(groupId, raceId);
		for(final FighterTypeEntity fighterType : race.getFighterTypes()) {
			if(fighterType.hasId(fighterTypeId)){
				return fighterType;
			}
		}
		throw new DataNotFoundException(String.format(
				"Fighter type %d (race %d, gang type %d, group %d)",
				fighterTypeId, raceId, gangTypeId, groupId));
	}
}

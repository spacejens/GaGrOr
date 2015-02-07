package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class FactionRepository {

	@Autowired
	GangTypeRepository gangTypeRepository;

	@Autowired
	FactionRepositoryQueries factionRepositoryQueries;

	public FactionEntity persist(final FactionEntity faction) {
		return factionRepositoryQueries.save(faction);
	}

	public FactionEntity load(final Long groupId, final Long gangTypeId, final Long factionId) {
		// TODO Load faction from ID using query, verify group after loading. Gang type ID no longer needed
		final GangTypeEntity gangType = gangTypeRepository.load(groupId, gangTypeId);
		for(final FactionEntity faction : gangType.getFactions()) {
			if(faction.hasId(factionId)) {
				return faction;
			}
		}
		throw new DataNotFoundException(String.format("Faction %d (gang type %d, group %d)",
				factionId, gangTypeId, groupId));
	}
}

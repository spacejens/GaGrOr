package com.gagror.data.wh40kskirmish.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionRepository;

@Repository
public class GangRepository {

	@Autowired
	FactionRepository factionRepository;

	@Autowired
	GangRepositoryQueries gangRepositoryQueries;

	public GangEntity persist(final GangEntity gang) {
		return gangRepositoryQueries.save(gang);
	}

	public GangEntity load(
			final Long groupId, final Long gangTypeId, final Long factionId, final Long gangId) {
		// TODO Load gang from ID using query, verify group after loading. Gang type and faction IDs no longer needed
		final FactionEntity faction = factionRepository.load(groupId, gangTypeId, factionId);
		for(final GangEntity gang : faction.getGangs()) {
			if(gang.hasId(gangId)) {
				return gang;
			}
		}
		throw new DataNotFoundException(String.format("Gang %d (faction %d, gang type %d, group %d)",
				gangId, factionId, gangTypeId, groupId));
	}
}

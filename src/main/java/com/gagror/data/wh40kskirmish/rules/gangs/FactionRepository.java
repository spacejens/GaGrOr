package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class FactionRepository {

	@Autowired
	FactionRepositoryQueries factionRepositoryQueries;

	public FactionEntity persist(final FactionEntity faction) {
		return factionRepositoryQueries.save(faction);
	}

	public FactionEntity load(final Long groupId, final Long factionId) {
		final FactionEntity faction = factionRepositoryQueries.findOne(factionId);
		if(null != faction && faction.getGroup().hasId(groupId)) {
			return faction;
		}
		throw new DataNotFoundException(String.format("Faction %d (group %d)",
				factionId, groupId));
	}
}

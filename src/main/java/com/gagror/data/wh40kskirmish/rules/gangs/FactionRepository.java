package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FactionRepository {

	@Autowired
	FactionRepositoryQueries factionRepositoryQueries;

	public FactionEntity persist(final FactionEntity faction) {
		return factionRepositoryQueries.save(faction);
	}
}

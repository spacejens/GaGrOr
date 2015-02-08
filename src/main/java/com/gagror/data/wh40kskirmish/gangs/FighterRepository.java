package com.gagror.data.wh40kskirmish.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class FighterRepository {

	@Autowired
	FighterRepositoryQueries fighterRepositoryQueries;

	public FighterEntity persist(final FighterEntity fighter) {
		return fighterRepositoryQueries.save(fighter);
	}

	public FighterEntity load(final Long groupId, final Long fighterId) {
		final FighterEntity fighter = fighterRepositoryQueries.findOne(fighterId);
		if(null != fighter && fighter.getGroup().hasId(groupId)) {
			return fighter;
		}
		throw new DataNotFoundException(String.format("Fighter %d (group %d)", fighterId, groupId));
	}
}

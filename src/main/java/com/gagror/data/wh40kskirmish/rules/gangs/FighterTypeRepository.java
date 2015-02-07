package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class FighterTypeRepository {

	@Autowired
	FighterTypeRepositoryQueries fighterTypeRepositoryQueries;

	public FighterTypeEntity persist(final FighterTypeEntity fighterType) {
		return fighterTypeRepositoryQueries.save(fighterType);
	}

	public FighterTypeEntity load(final Long groupId, final Long fighterTypeId) {
		final FighterTypeEntity fighterType = fighterTypeRepositoryQueries.findOne(fighterTypeId);
		if(null != fighterType && fighterType.getGroup().hasId(groupId)) {
			return fighterType;
		}
		throw new DataNotFoundException(String.format("Fighter type %d (group %d)", fighterTypeId, groupId));
	}
}

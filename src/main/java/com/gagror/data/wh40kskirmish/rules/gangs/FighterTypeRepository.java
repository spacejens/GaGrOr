package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FighterTypeRepository {

	@Autowired
	FighterTypeRepositoryQueries fighterTypeRepositoryQueries;

	public FighterTypeEntity persist(final FighterTypeEntity fighterType) {
		return fighterTypeRepositoryQueries.save(fighterType);
	}
}

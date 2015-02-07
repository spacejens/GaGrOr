package com.gagror.data.wh40kskirmish.rules.experience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ExperienceLevelRepository {

	@Autowired
	ExperienceLevelRepositoryQueries experienceLevelRepositoryQueries;

	public ExperienceLevelEntity persist(final ExperienceLevelEntity experienceLevel) {
		return experienceLevelRepositoryQueries.save(experienceLevel);
	}

	public void delete(final ExperienceLevelEntity experienceLevel) {
		experienceLevelRepositoryQueries.delete(experienceLevel);
	}
}

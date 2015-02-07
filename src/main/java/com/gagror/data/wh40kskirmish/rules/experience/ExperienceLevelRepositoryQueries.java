package com.gagror.data.wh40kskirmish.rules.experience;

import org.springframework.data.repository.Repository;

interface ExperienceLevelRepositoryQueries extends Repository<ExperienceLevelEntity, Long> {

	ExperienceLevelEntity save(final ExperienceLevelEntity experienceLevel);

	void delete(final ExperienceLevelEntity experienceLevel);
}

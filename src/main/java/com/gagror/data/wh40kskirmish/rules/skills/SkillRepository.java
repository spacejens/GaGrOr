package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SkillRepository {

	@Autowired
	SkillRepositoryQueries skillRepositoryQueries;

	public SkillEntity persist(final SkillEntity skill) {
		return skillRepositoryQueries.save(skill);
	}
}

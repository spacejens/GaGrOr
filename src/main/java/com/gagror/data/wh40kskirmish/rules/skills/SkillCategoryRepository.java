package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SkillCategoryRepository {

	@Autowired
	SkillCategoryRepositoryQueries skillCategoryRepositoryQueries;

	public SkillCategoryEntity persist(final SkillCategoryEntity skillCategory) {
		return skillCategoryRepositoryQueries.save(skillCategory);
	}
}

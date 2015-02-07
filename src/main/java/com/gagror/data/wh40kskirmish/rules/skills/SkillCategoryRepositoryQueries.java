package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.data.repository.Repository;

interface SkillCategoryRepositoryQueries extends Repository<SkillCategoryEntity, Long> {

	SkillCategoryEntity findOne(final Long skillCategoryId);

	SkillCategoryEntity save(final SkillCategoryEntity skillCategory);
}

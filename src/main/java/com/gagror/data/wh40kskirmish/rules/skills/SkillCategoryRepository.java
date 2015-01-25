package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.data.repository.Repository;

public interface SkillCategoryRepository extends Repository<SkillCategoryEntity, Long> {

	SkillCategoryEntity save(final SkillCategoryEntity gangType);
}

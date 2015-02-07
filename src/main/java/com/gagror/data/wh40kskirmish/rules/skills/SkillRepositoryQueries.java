package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.data.repository.Repository;

interface SkillRepositoryQueries extends Repository<SkillEntity, Long> {

	SkillEntity findOne(final Long skillId);

	SkillEntity save(final SkillEntity skill);
}

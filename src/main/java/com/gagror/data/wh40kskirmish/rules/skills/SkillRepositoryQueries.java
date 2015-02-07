package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.data.repository.Repository;

interface SkillRepositoryQueries extends Repository<SkillEntity, Long> {

	SkillEntity save(final SkillEntity skill);
}

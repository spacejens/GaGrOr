package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.data.repository.Repository;

public interface SkillRepository extends Repository<SkillEntity, Long> {

	SkillEntity save(final SkillEntity gangType);
}

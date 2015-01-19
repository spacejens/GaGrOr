package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.data.repository.Repository;

public interface Wh40kSkirmishSkillCategoryRepository extends Repository<Wh40kSkirmishSkillCategoryEntity, Long> {

	Wh40kSkirmishSkillCategoryEntity save(final Wh40kSkirmishSkillCategoryEntity gangType);
}

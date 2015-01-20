package com.gagror.data.wh40kskirmish.rules.skills;

import org.springframework.data.repository.Repository;

public interface Wh40kSkirmishSkillRepository extends Repository<Wh40kSkirmishSkillEntity, Long> {

	Wh40kSkirmishSkillEntity save(final Wh40kSkirmishSkillEntity gangType);
}

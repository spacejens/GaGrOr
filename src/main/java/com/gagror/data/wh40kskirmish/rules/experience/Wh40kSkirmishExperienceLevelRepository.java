package com.gagror.data.wh40kskirmish.rules.experience;

import org.springframework.data.repository.Repository;

public interface Wh40kSkirmishExperienceLevelRepository extends Repository<Wh40kSkirmishExperienceLevelEntity, Long> {

	Wh40kSkirmishExperienceLevelEntity save(final Wh40kSkirmishExperienceLevelEntity gangType);
}

package com.gagror.data.wh40kskirmish;

import org.springframework.data.repository.Repository;

public interface Wh40kSkirmishFactionRepository extends Repository<Wh40kSkirmishFactionEntity, Long> {

	Wh40kSkirmishFactionEntity save(final Wh40kSkirmishFactionEntity gangType);
}

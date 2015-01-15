package com.gagror.data.wh40kskirmish;

import org.springframework.data.repository.Repository;

public interface Wh40kSkirmishRaceRepository extends Repository<Wh40kSkirmishRaceEntity, Long> {

	Wh40kSkirmishRaceEntity save(final Wh40kSkirmishRaceEntity race);
}

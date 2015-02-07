package com.gagror.data.wh40kskirmish.rules.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;

@Repository
public class GangTypeRepository {

	@Autowired
	RulesRepository rulesRepository;

	@Autowired
	GangTypeRepositoryQueries gangTypeRepositoryQueries;

	public GangTypeEntity persist(final GangTypeEntity gangType) {
		return gangTypeRepositoryQueries.save(gangType);
	}

	public GangTypeEntity load(final Long groupId, final Long gangTypeId) {
		// TODO Load gang type from ID using query, verify group after loading
		final Wh40kSkirmishRulesEntity rules = rulesRepository.load(groupId);
		for(final GangTypeEntity gangType : rules.getGangTypes()) {
			if(gangType.hasId(gangTypeId)) {
				return gangType;
			}
		}
		throw new DataNotFoundException(String.format("Gang type %d (group %d)", gangTypeId, groupId));
	}
}

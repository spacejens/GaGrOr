package com.gagror.data.wh40kskirmish.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class GangRepository {

	@Autowired
	GangRepositoryQueries gangRepositoryQueries;

	public GangEntity persist(final GangEntity gang) {
		return gangRepositoryQueries.save(gang);
	}

	public GangEntity load(final Long gangId) {
		final GangEntity gang = gangRepositoryQueries.findOne(gangId);
		if(null != gang) {
			return gang;
		}
		throw new DataNotFoundException(String.format("Gang %d", gangId));
	}

	public GangEntity load(final Long groupId, final Long gangId) {
		final GangEntity gang = gangRepositoryQueries.findOne(gangId);
		if(null != gang && gang.getGroup().hasId(groupId)) {
			return gang;
		}
		throw new DataNotFoundException(String.format("Gang %d (group %d)", gangId, groupId));
	}
}

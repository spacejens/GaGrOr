package com.gagror.data.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class GroupRepository {

	@Autowired
	GroupRepositoryQueries groupRepositoryQueries;

	public GroupEntity load(final Long groupId) {
		final GroupEntity group = groupRepositoryQueries.findOne(groupId);
		if(null == group) {
			throw new DataNotFoundException(String.format("Group %d", groupId));
		}
		return group;
	}

	public GroupEntity persist(final GroupEntity group) {
		return groupRepositoryQueries.save(group);
	}

	public List<GroupEntity> listViewableByAnyone() {
		return groupRepositoryQueries.findByViewableByAnyone(true);
	}
}

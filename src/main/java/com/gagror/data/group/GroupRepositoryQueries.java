package com.gagror.data.group;

import java.util.List;

import org.springframework.data.repository.Repository;

interface GroupRepositoryQueries extends Repository<GroupEntity, Long> {

	GroupEntity findOne(final Long groupId);

	GroupEntity save(final GroupEntity group);

	List<GroupEntity> findByViewableByAnyone(final boolean viewableByAnyone);
}

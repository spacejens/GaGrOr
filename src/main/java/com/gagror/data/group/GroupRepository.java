package com.gagror.data.group;

import org.springframework.data.repository.Repository;

public interface GroupRepository extends Repository<GroupEntity, Long> {

	GroupEntity findOne(final Long groupId);

	GroupEntity save(final GroupEntity group);
}

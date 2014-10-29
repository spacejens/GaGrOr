package com.gagror.data.group;

import org.springframework.data.repository.Repository;

public interface GroupRepository extends Repository<GroupEntity, Long> {

	GroupEntity save(final GroupEntity group);
}

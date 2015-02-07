package com.gagror.data.group;

import org.springframework.data.repository.Repository;

interface GroupMemberRepositoryQueries extends Repository<GroupMemberEntity, Long> {

	GroupMemberEntity save(final GroupMemberEntity groupMember);

	void delete(final GroupMemberEntity groupMember);
}

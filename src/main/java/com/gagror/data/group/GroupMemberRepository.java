package com.gagror.data.group;

import org.springframework.data.repository.Repository;

public interface GroupMemberRepository extends Repository<GroupMemberEntity, Long> {

	GroupMemberEntity save(final GroupMemberEntity groupMember);
}

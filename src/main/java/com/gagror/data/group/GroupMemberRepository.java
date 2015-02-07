package com.gagror.data.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GroupMemberRepository {

	@Autowired
	GroupMemberRepositoryQueries groupMemberRepositoryQueries;

	public GroupMemberEntity persist(final GroupMemberEntity groupMember) {
		return groupMemberRepositoryQueries.save(groupMember);
	}

	public void delete(final GroupMemberEntity groupMember) {
		groupMemberRepositoryQueries.delete(groupMember);
	}
}

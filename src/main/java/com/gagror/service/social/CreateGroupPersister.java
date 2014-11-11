package com.gagror.service.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.group.GroupCreateInput;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupMemberRepository;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.MemberType;
import com.gagror.service.AbstractPersister;
import com.gagror.service.accesscontrol.AccessControlService;

@Service
public class CreateGroupPersister extends AbstractPersister<GroupCreateInput, GroupEntity> {

	@Autowired
	AccessControlService accessControlService;

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	GroupMemberRepository groupMemberRepository;

	@Override
	protected void validateForm(final GroupCreateInput form, final BindingResult bindingResult) {
		// There are no rules that are not already handled by form bean validation
	}

	@Override
	protected boolean isCreateNew(final GroupCreateInput form) {
		return true;
	}

	@Override
	protected GroupEntity createNew(final GroupCreateInput form) {
		return new GroupEntity(form.getName());
	}

	@Override
	protected void updateValues(final GroupCreateInput form, final GroupEntity entity) {
		new GroupMemberEntity(
				entity,
				accessControlService.getRequestAccountEntity(),
				MemberType.OWNER);
	}

	@Override
	protected GroupEntity makePersistent(final GroupEntity entity) {
		return groupRepository.save(entity);
	}

	@Override
	protected void postPersistenceUpdate(final GroupEntity entity) {
		for(final GroupMemberEntity memberEntity : entity.getGroupMemberships()) {
			groupMemberRepository.save(memberEntity);
		}
	}
}

package com.gagror.service.social;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.AbstractEntity;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.ContactEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupInviteInput;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupMemberRepository;
import com.gagror.data.group.GroupMembershipChangeException;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.MemberType;
import com.gagror.service.AbstractIdentifiablePersister;
import com.gagror.service.accesscontrol.AccessControlService;

@Service
public class InviteGroupPersister extends AbstractIdentifiablePersister<GroupInviteInput, GroupEntity, AbstractEntity> {

	@Autowired
	AccessControlService accessControlService;

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	GroupMemberRepository groupMemberRepository;

	@Override
	protected void validateForm(final GroupInviteInput form, final BindingResult bindingResult) {
		final Set<Long> contactAccountIds = new HashSet<>();
		for(final ContactEntity contact : accessControlService.getRequestAccountEntity().getContacts()) {
			if(contact.getContactType().isContact()) {
				contactAccountIds.add(contact.getContact().getId());
			}
		}
		for(final Long invited : form.getSelected()) {
			if(! contactAccountIds.contains(invited)) {
				throw new GroupMembershipChangeException(String.format("Invited account %d is not a contact, cannot invite", invited));
			}
		}
	}

	@Override
	protected boolean isCreateNew(final GroupInviteInput form) {
		return false;
	}

	@Override
	protected GroupEntity loadExisting(final GroupInviteInput form, final AbstractEntity context) {
		return groupRepository.load(form.getId());
	}

	@Override
	protected void updateValues(final GroupInviteInput form, final GroupEntity entity) {
		final Set<AccountEntity> existingMembers = new HashSet<>();
		for(final GroupMemberEntity member : entity.getGroupMemberships()) {
			existingMembers.add(member.getAccount());
		}
		for(final Long invited : form.getSelected()) {
			final AccountEntity account = accountRepository.load(invited);
			if(! existingMembers.contains(account)) {
				groupMemberRepository.persist(new GroupMemberEntity(
						entity,
						account,
						MemberType.INVITED));
			}
		}
	}
}

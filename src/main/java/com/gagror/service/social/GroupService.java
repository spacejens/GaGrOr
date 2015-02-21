package com.gagror.service.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.ContactEntity;
import com.gagror.data.account.ContactType;
import com.gagror.data.group.GroupEditOutput;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupListOutput;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupMemberRepository;
import com.gagror.data.group.GroupMembershipChangeException;
import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.GroupViewMembersOutput;
import com.gagror.data.group.MemberType;
import com.gagror.data.group.NotGroupMemberException;
import com.gagror.service.accesscontrol.AccessControlService;

@Service
@Transactional
@CommonsLog
public class GroupService {

	@Autowired
	AccessControlService accessControlService;

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	GroupMemberRepository groupMemberRepository;

	@Autowired
	AccountService accountService;

	public List<GroupListOutput> loadGroupList() {
		log.debug("Loading group list");
		final List<GroupListOutput> output = new ArrayList<>();
		for(final GroupMemberEntity member : accessControlService.getRequestAccountEntity().getGroupMemberships()) {
			if(member.getMemberType().isMember()) {
				output.add(new GroupListOutput(member));
			}
		}
		Collections.sort(output);
		return output;
	}

	public List<GroupListOutput> loadInvitationsList() {
		log.debug("Loading invitations list");
		final List<GroupListOutput> output = new ArrayList<>();
		for(final GroupMemberEntity member : accessControlService.getRequestAccountEntity().getGroupMemberships()) {
			if(member.getMemberType().isInvitation()) {
				output.add(new GroupListOutput(member));
			}
		}
		Collections.sort(output);
		return output;
	}

	public List<GroupListOutput> loadPublicGroupList() {
		log.debug("Loading public group list");
		final List<GroupListOutput> output = new ArrayList<>();
		final List<GroupEntity> groups = groupRepository.listViewableByAnyone();
		for(final GroupEntity group : groups) {
			output.add(new GroupListOutput(group));
		}
		Collections.sort(output);
		return output;
	}

	public GroupReferenceOutput viewGroup(final Long groupId) {
		return new GroupReferenceOutput(groupRepository.load(groupId));
	}

	public GroupEditOutput editGroup(final Long groupId) {
		return new GroupEditOutput(groupRepository.load(groupId));
	}

	public GroupViewMembersOutput viewGroupMembers(final Long groupId) {
		final GroupEntity group = groupRepository.load(groupId);
		log.debug(String.format("Loaded group %s for viewing", group));
		return new GroupViewMembersOutput(group);
	}

	public List<AccountReferenceOutput> loadPossibleUsersToInvite(final Long groupId) {
		final GroupEntity group = groupRepository.load(groupId);
		// Find the group of users who are already invited or members
		final Set<AccountEntity> groupMemberAccounts = findGroupMemberAccounts(group, false);
		// Find the possible users
		final List<AccountReferenceOutput> output = new ArrayList<>();
		for(final ContactEntity contact : accessControlService.getRequestAccountEntity().getContacts()) {
			// Filter out group members, and contacts who have not been accepted
			if(! groupMemberAccounts.contains(contact.getContact()) && contact.getContactType().isContact()) {
				output.add(new AccountReferenceOutput(contact.getContact()));
			}
		}
		Collections.sort(output);
		return output;
	}

	private Set<AccountEntity> findGroupMemberAccounts(final GroupEntity group, final boolean onlyFullMembers) {
		final Set<AccountEntity> groupMemberAccounts = new HashSet<>();
		for(final GroupMemberEntity membership : group.getGroupMemberships()) {
			if(! onlyFullMembers || membership.getMemberType().isMember()) {
				groupMemberAccounts.add(membership.getAccount());
			}
		}
		return groupMemberAccounts;
	}

	public void accept(final Long memberId) {
		final GroupMemberEntity invitation = findInvitation(memberId);
		if(null != invitation) {
			invitation.setMemberType(MemberType.MEMBER);
			// Add group members as contacts
			final AccountEntity requestAccount = accessControlService.getRequestAccountEntity();
			for(final AccountEntity groupMember : findGroupMemberAccounts(invitation.getGroup(), true)) {
				if(! requestAccount.equals(groupMember)) {
					boolean foundContact = false;
					for(final ContactEntity contact : requestAccount.getContacts()) {
						if(contact.getContact().equals(groupMember)) {
							foundContact = true;
							if(contact.getContactType().isRequest()) {
								// Auto-accept and mirror the requested contact
								log.debug(String.format("Auto-accepting and mirroring requested contact: %s", contact));
								contact.setContactType(ContactType.AUTOMATIC);
								accountService.mirrorContact(contact);
							}
						}
					}
					for(final ContactEntity incoming : requestAccount.getIncomingContacts()) {
						if(incoming.getOwner().equals(groupMember) && incoming.getContactType().isRequest()) {
							foundContact = true;
							// Auto-accept and mirror the incoming contact request
							log.debug(String.format("Auto-accepting and mirroring incoming contact request: %s", incoming));
							incoming.setContactType(ContactType.AUTOMATIC);
							accountService.mirrorContact(incoming);
						}
					}
					if(! foundContact) {
						// Create and mirror the contact with the non-contact user
						log.debug(String.format("Creating and mirroring contact for %s and %s", requestAccount, groupMember));
						final ContactEntity contact = accountService.createContact(requestAccount, ContactType.AUTOMATIC, groupMember);
						accountService.mirrorContact(contact);
					}
				}
			}
		}
		// If invitation does no longer exist, just silently ignore it
	}

	public void decline(final Long memberId) {
		final GroupMemberEntity invitation = findInvitation(memberId);
		if(null != invitation) {
			invitation.getAccount().getGroupMemberships().remove(invitation);
			invitation.getGroup().getGroupMemberships().remove(invitation);
			groupMemberRepository.delete(invitation);
		}
		// If invitation does no longer exist, just silently ignore it
	}

	private GroupMemberEntity findInvitation(final Long memberId) {
		for(final GroupMemberEntity membership : accessControlService.getRequestAccountEntity().getGroupMemberships()) {
			if(membership.hasId(memberId) && membership.getMemberType().isInvitation()) {
				return membership;
			}
		}
		return null;
	}

	public void leave(final Long groupId) {
		for(final GroupMemberEntity membership : accessControlService.getRequestAccountEntity().getGroupMemberships()) {
			if(membership.getGroup().hasId(groupId)) {
				// If the request account is the only owner, we cannot leave the group
				if(membership.getMemberType().isOwner()) {
					int countOwners = 0;
					for(final GroupMemberEntity member : membership.getGroup().getGroupMemberships()) {
						if(member.getMemberType().isOwner()) {
							countOwners++;
						}
					}
					if(1 == countOwners) {
						throw new GroupMembershipChangeException(String.format("Only owner, cannot leave group %s", membership.getGroup()));
					}
				}
				// Remove the membership
				deleteMembership(membership);
				return;
			}
		}
	}

	private void deleteMembership(final GroupMemberEntity membership) {
		membership.getGroup().getGroupMemberships().remove(membership);
		membership.getAccount().getGroupMemberships().remove(membership);
		groupMemberRepository.delete(membership);
	}

	public void promote(final Long groupId, final Long accountId) {
		final GroupMemberEntity member = findAnotherGroupMemberFromMemberships(groupId, accountId);
		if(null == member || ! member.getMemberType().isMember()) {
			throw new GroupMembershipChangeException(String.format("Cannot promote, account %d is not a member of group %d", accountId, groupId));
		}
		member.setMemberType(MemberType.OWNER);
	}

	public void demote(final Long groupId, final Long accountId) {
		final GroupMemberEntity member = findAnotherGroupMemberFromMemberships(groupId, accountId);
		if(null == member || ! member.getMemberType().isMember()) {
			throw new GroupMembershipChangeException(String.format("Cannot demote, account %d is not a member of group %d", accountId, groupId));
		}
		member.setMemberType(MemberType.MEMBER);
	}

	public void remove(final Long groupId, final Long accountId) {
		final GroupMemberEntity member = findAnotherGroupMemberFromMemberships(groupId, accountId);
		if(null == member) {
			// Silently ignore that the account could not be found in the group, that's the end result we want anyway
			return;
		}
		deleteMembership(member);
	}

	private GroupMemberEntity findAnotherGroupMemberFromMemberships(final Long groupId, final Long accountId) {
		final AccountEntity requestAccount = accessControlService.getRequestAccountEntity();
		if(requestAccount.hasId(accountId)) {
			throw new GroupMembershipChangeException("This action can only be performed on other accounts");
		}
		GroupEntity group = null;
		for(final GroupMemberEntity membership : requestAccount.getGroupMemberships()) {
			if(membership.getGroup().hasId(groupId)) {
				group = membership.getGroup();
				break;
			}
		}
		if(null == group) {
			throw new NotGroupMemberException(String.format("Request account is not a member of group %d", groupId));
		}
		for(final GroupMemberEntity membership : group.getGroupMemberships()) {
			if(membership.getAccount().hasId(accountId)) {
				return membership;
			}
		}
		// Member not found
		return null;
	}
}

package com.gagror.controller.system;

import javax.validation.Valid;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import com.gagror.controller.AbstractController;
import com.gagror.data.group.GroupCreateInput;
import com.gagror.data.group.GroupEditInput;
import com.gagror.data.group.GroupEditOutput;
import com.gagror.data.group.GroupInviteInput;
import com.gagror.data.group.GroupType;
import com.gagror.service.social.CreateGroupPersister;
import com.gagror.service.social.EditGroupPersister;
import com.gagror.service.social.GroupService;
import com.gagror.service.social.InviteGroupPersister;

@Controller
@RequestMapping("/groups")
@CommonsLog
public class GroupsController extends AbstractController {

	@Autowired
	GroupService groupService;

	@Autowired
	CreateGroupPersister createGroupPersister;

	@Autowired
	EditGroupPersister editGroupPersister;

	@Autowired
	InviteGroupPersister inviteGroupPersister;

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/list")
	public String listGroups(final Model model) {
		log.info("Viewing groups list page");
		model.addAttribute("groups", groupService.loadGroupList());
		model.addAttribute("invitations", groupService.loadInvitationsList());
		return "groups";
	}

	@PreAuthorize(IS_PUBLIC)
	@RequestMapping("/public")
	public String listPublicGroups(final Model model) {
		log.info("Viewing groups public list page");
		model.addAttribute("publicGroups", groupService.loadPublicGroupList());
		return "groups_public";
	}

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String createGroupForm(
			@Valid @ModelAttribute("groupCreateForm") final GroupCreateInput groupCreateForm,
			final Model model) {
		log.info("Viewing create group form");
		groupCreateForm.setGroupType(GroupType.SOCIAL);
		return showCreateGroupForm(model);
	}

	private String showCreateGroupForm(final Model model) {
		model.addAttribute("groupTypes", GroupType.values());
		return "create_group";
	}

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public Object createGroup(
			final Model model,
			@Valid @ModelAttribute("groupCreateForm") final GroupCreateInput groupCreateForm,
			final BindingResult bindingResult) {
		if(createGroupPersister.save(groupCreateForm, bindingResult)) {
			return redirect("/groups/list");
		} else {
			return showCreateGroupForm(model);
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/view/{" + ATTR_GROUP_ID + "}")
	public String viewGroup(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		// TODO View group type when viewing group
		return "view_group";
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/members/{" + ATTR_GROUP_ID + "}")
	public String groupMembers(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing members of group %d", groupId));
		model.addAttribute("group", groupService.viewGroupMembers(groupId));
		return "group_members";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/edit/{" + ATTR_GROUP_ID + "}", method=RequestMethod.GET)
	public String editForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model) {
		log.info(String.format("Showing settings edit form for group %d", groupId));
		final GroupEditOutput currentState = groupService.editGroup(groupId);
		model.addAttribute("group", currentState);
		model.addAttribute("groupEditForm", new GroupEditInput(currentState));
		// TODO Show group type (readonly) when editing group?
		return "edit_group";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/edit/{" + ATTR_GROUP_ID + "}", method=RequestMethod.POST)
	public Object edit(
			@Valid @ModelAttribute("groupEditForm") final GroupEditInput groupEditForm,
			final BindingResult bindingResult,
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model) {
		if(! groupId.equals(groupEditForm.getId())) {
			log.error(String.format("Group ID URL (%d) and form (%d) mismatch when attempting to edit group", groupId, groupEditForm.getId()));
			throw new IllegalArgumentException("Unexpected group ID in edit form");
		}
		if(editGroupPersister.save(groupEditForm, bindingResult)) {
			return redirect(String.format("/groups/view/%d", groupId));
		} else {
			final GroupEditOutput currentState = groupService.editGroup(groupId);
			model.addAttribute("group", currentState);
			return "edit_group";
		}
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/invite/{" + ATTR_GROUP_ID + "}", method=RequestMethod.GET)
	public String inviteForm(
			@Valid @ModelAttribute("groupInviteForm") final GroupInviteInput groupInviteForm,
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model) {
		log.info(String.format("Showing member invite form for group %d", groupId));
		groupInviteForm.setId(groupId);
		return showInviteForm(groupId, model);
	}

	private String showInviteForm(final Long groupId, final Model model) {
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("candidates", groupService.loadPossibleUsersToInvite(groupId));
		return "group_invite";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/invite/{" + ATTR_GROUP_ID + "}", method=RequestMethod.POST)
	public Object invite(
			@Valid @ModelAttribute("groupInviteForm") final GroupInviteInput groupInviteForm,
			final BindingResult bindingResult,
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model) {
		if(! groupId.equals(groupInviteForm.getId())) {
			log.error(String.format("Group ID URL (%d) and form (%d) mismatch when attempting to invite users", groupId, groupInviteForm.getId()));
			throw new IllegalArgumentException("Unexpected group ID in invite form");
		}
		if(inviteGroupPersister.save(groupInviteForm, bindingResult)) {
			log.info(String.format("Invited users %s to group %d", groupInviteForm.getSelected(), groupId));
			return redirect(String.format("/groups/members/%d", groupId));
		} else {
			log.warn(String.format("Failed to invite users to group %d: %s", groupId, groupInviteForm));
			return showInviteForm(groupId, model);
		}
	}

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/accept/{memberId}")
	public RedirectView accept(@PathVariable("memberId") final Long memberId) {
		groupService.accept(memberId);
		return redirect("/groups/list");
	}

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/decline/{memberId}")
	public RedirectView decline(@PathVariable("memberId") final Long memberId) {
		groupService.decline(memberId);
		return redirect("/groups/list");
	}

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/leave/{" + ATTR_GROUP_ID + "}")
	public RedirectView leave(@PathVariable(ATTR_GROUP_ID) final Long groupId) {
		groupService.leave(groupId);
		return redirect("/groups/list");
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/promote/{" + ATTR_ACCOUNT_ID + "}")
	public RedirectView promote(@PathVariable(ATTR_GROUP_ID) final Long groupId, @PathVariable(ATTR_ACCOUNT_ID) final Long accountId) {
		groupService.promote(groupId, accountId);
		return redirect(String.format("/groups/members/%d", groupId));
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/demote/{" + ATTR_ACCOUNT_ID + "}")
	public RedirectView demote(@PathVariable(ATTR_GROUP_ID) final Long groupId, @PathVariable(ATTR_ACCOUNT_ID) final Long accountId) {
		groupService.demote(groupId, accountId);
		return redirect(String.format("/groups/members/%d", groupId));
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/remove/{" + ATTR_ACCOUNT_ID + "}")
	public RedirectView remove(@PathVariable(ATTR_GROUP_ID) final Long groupId, @PathVariable(ATTR_ACCOUNT_ID) final Long accountId) {
		groupService.remove(groupId, accountId);
		return redirect(String.format("/groups/members/%d", groupId));
	}
}

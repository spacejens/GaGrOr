package com.gagror.controller.system;

import static com.gagror.data.account.SecurityRoles.IS_LOGGED_IN;

import java.util.List;

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
import com.gagror.data.group.GroupInviteInput;
import com.gagror.data.group.GroupListOutput;
import com.gagror.service.social.GroupService;

@Controller
@RequestMapping("/groups")
@CommonsLog
public class GroupsController extends AbstractController {

	@Autowired
	GroupService groupService;

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/list")
	public String listGroups() {
		log.info("Viewing groups list page");
		return "groups";
	}

	@PreAuthorize(IS_LOGGED_IN)
	@ModelAttribute("groups")
	public List<GroupListOutput> getGroups() {
		return groupService.loadGroupList();
	}

	@PreAuthorize(IS_LOGGED_IN)
	@ModelAttribute("invitations")
	public List<GroupListOutput> getInvitations() {
		return groupService.loadInvitationsList();
	}

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String createGroupForm(@Valid @ModelAttribute("groupCreateForm") final GroupCreateInput groupCreateForm) {
		log.info("Viewing create group form");
		return "create_group";
	}

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public Object createGroup(
			@Valid @ModelAttribute("groupCreateForm") final GroupCreateInput groupCreateForm,
			final BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			log.info(String.format("Failed to create group '%s', form had errors", groupCreateForm.getName()));
			return "create_group";
		}
		groupService.createGroup(groupCreateForm, bindingResult);
		if(bindingResult.hasErrors()) {
			log.info(String.format("Failed to create group '%s', rejected by service layer", groupCreateForm.getName()));
			return "create_group";
		}
		log.info(String.format("Successfully created group '%s'", groupCreateForm.getName()));
		return redirect("/groups/list");
	}

	@PreAuthorize(IS_LOGGED_IN + " and hasPermission(#groupId, 'viewGroup')")
	@RequestMapping("/view/{groupId}")
	public String viewGroup(@PathVariable("groupId") final Long groupId, final Model model) {
		log.info(String.format("Viewing group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		return "view_group";
	}

	@PreAuthorize(IS_LOGGED_IN + " and hasPermission(#groupId, 'viewGroup')")
	@RequestMapping("/members/{groupId}")
	public String groupMembers(@PathVariable("groupId") final Long groupId, final Model model) {
		log.info(String.format("Viewing members of group %d", groupId));
		model.addAttribute("group", groupService.viewGroupMembers(groupId));
		return "group_members";
	}

	@PreAuthorize(IS_LOGGED_IN + " and hasPermission(#groupId, 'adminGroup')")
	@RequestMapping(value="/invite/{groupId}", method=RequestMethod.GET)
	public String inviteForm(
			@Valid @ModelAttribute("groupInviteForm") final GroupInviteInput groupInviteForm,
			@PathVariable("groupId") final Long groupId,
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

	@PreAuthorize(IS_LOGGED_IN + " and hasPermission(#groupId, 'adminGroup')")
	@RequestMapping(value="/invite/{groupId}", method=RequestMethod.POST)
	public Object invite(
			@Valid @ModelAttribute("groupInviteForm") final GroupInviteInput groupInviteForm,
			final BindingResult bindingResult,
			@PathVariable("groupId") final Long groupId,
			final Model model) {
		if(! groupId.equals(groupInviteForm.getId())) {
			log.error(String.format("Group ID URL (%d) and form (%d) mismatch when attempting to invite users", groupId, groupInviteForm.getId()));
			throw new IllegalArgumentException("Unexpected group ID in invite form");
		}
		if(bindingResult.hasErrors()) {
			log.info(String.format("Failed to invite users to group %d, form had errors", groupId));
			return showInviteForm(groupId, model);
		}
		groupService.sendInvitations(groupInviteForm, bindingResult);
		if(bindingResult.hasErrors()) {
			log.info(String.format("Failed to invite users to group %d, rejected by service layer", groupId));
			return showInviteForm(groupId, model);
		}
		log.info(String.format("Invited users %s to group %d", groupInviteForm.getSelected(), groupId));
		return redirect(String.format("/groups/members/%d", groupId));
		// TODO Extract persist form boilerplate (here and elsewhere) to a new type of persister service, with a common superclass defining the flow
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
	@RequestMapping("/leave/{groupId}")
	public RedirectView leave(@PathVariable("groupId") final Long groupId) {
		groupService.leave(groupId);
		return redirect("/groups/list");
	}

	// TODO Make it possible to edit the group membership level of members, or to kick members out
}

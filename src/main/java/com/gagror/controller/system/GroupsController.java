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

import com.gagror.controller.AbstractController;
import com.gagror.data.group.GroupCreateInput;
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

	// TODO Allow inviting other users to your group

	// TODO Make it possible to accept invitations to groups

	// TODO Make it possible to decline invitations to groups

	// TODO Make it possible to edit the group membership level of members, or to kick members out

	// TODO Make it possible to leave groups (unless you are the only group owner)
}

package com.gagror.controller;

import static com.gagror.data.account.SecurityRoles.IS_LOGGED_IN;

import java.util.List;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gagror.data.group.GroupListOutput;
import com.gagror.service.group.GroupService;

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

	// TODO List received invitations for groups on the page

	// TODO Allow creating groups on the page

	// TODO Allow inviting other users to your group
}

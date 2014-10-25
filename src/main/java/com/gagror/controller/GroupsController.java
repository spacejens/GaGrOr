package com.gagror.controller;

import static com.gagror.data.account.SecurityRoles.IS_LOGGED_IN;
import lombok.extern.apachecommons.CommonsLog;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/groups")
@CommonsLog
public class GroupsController extends AbstractController {

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/list")
	public String listGroups() {
		log.info("Viewing groups list page");
		return "groups";
	}

	// TODO Add data model for groups and membership, with different membership status (invited, member, group owner)

	// TODO List groups on the page

	// TODO List received invitations for groups on the page

	// TODO Allow creating groups on the page

	// TODO Allow inviting other users to your group
}

package com.gagror.controller;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gagror.data.account.SecurityRoles;

@Controller
@RequestMapping("/profile")
@CommonsLog
public class ProfileController extends AbstractController {

	@PreAuthorize(SecurityRoles.IS_LOGGED_IN)
	@RequestMapping("/edit")
	public String editProfileForm() {
		log.info("Viewing edit profile form");
		return "profile";
	}
}

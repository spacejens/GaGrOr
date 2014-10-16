package com.gagror.controller;

import static com.gagror.data.account.SecurityRoles.IS_LOGGED_IN;
import lombok.extern.apachecommons.CommonsLog;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
@CommonsLog
public class ProfileController extends AbstractController {

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/edit")
	public String editProfileForm() {
		log.info("Viewing edit profile form");
		return "profile";
	}
}

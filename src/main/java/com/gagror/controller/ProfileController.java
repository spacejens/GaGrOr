package com.gagror.controller;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
@CommonsLog
public class ProfileController extends AbstractController {

	@RequestMapping("/edit")
	public String editProfileForm() {
		log.info("Viewing edit profile form");
		return "profile";
	}
}

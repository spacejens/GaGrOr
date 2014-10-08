package com.gagror.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController extends AbstractController {

	@RequestMapping("/edit")
	public String editProfileForm() {
		return "profile";
	}
}

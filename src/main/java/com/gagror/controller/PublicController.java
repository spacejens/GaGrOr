package com.gagror.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PublicController {

	@RequestMapping("/")
	public String about(final Model model) {
		return "about";
	}

	@RequestMapping("/login")
	public String login(final Model model) {
		return "login";
	}

	@RequestMapping("/register")
	public String register(final Model model) {
		return "register";
	}
}

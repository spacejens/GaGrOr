package com.gagror.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gagror.data.account.LoginCredentialsInput;
import com.gagror.service.accesscontrol.AccessControlService;

@Controller
@RequestMapping("/access")
public class AccessController {

	@Autowired
	AccessControlService accessControl;

	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loginForm(final Model model) {
		model.addAttribute("loginForm", new LoginCredentialsInput());
		return "login";
	}

	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String loginProcess(final Model model, final LoginCredentialsInput loginForm) {
		model.addAttribute("loginForm", loginForm);
		model.addAttribute("loggedInUser", accessControl.logIn(loginForm));
		return "login";
	}

	@RequestMapping("/register")
	public String register(final Model model) {
		return "register";
	}

	@ModelAttribute("currentUser")
	public String getCurrentUser() {
		// TODO Get current user from session data
		final LoginCredentialsInput loginCredentials = new LoginCredentialsInput();
		loginCredentials.setLogin("admin");
		loginCredentials.setPassword("password");
		return accessControl.logIn(loginCredentials);
	}
}

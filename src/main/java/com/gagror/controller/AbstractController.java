package com.gagror.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.gagror.data.account.LoginCredentialsInput;
import com.gagror.service.accesscontrol.AccessControlService;

public abstract class AbstractController {

	@Autowired
	AccessControlService accessControl;

	@ModelAttribute("currentUser")
	public String getCurrentUser() {
		// TODO Get current user from session data
		final LoginCredentialsInput loginCredentials = new LoginCredentialsInput();
		loginCredentials.setLogin("admin");
		loginCredentials.setPassword("password");
		return accessControl.logIn(loginCredentials);
	}
}

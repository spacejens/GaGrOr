package com.gagror.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.gagror.service.accesscontrol.AccessControlService;
import com.gagror.service.accesscontrol.SessionCredentialsComponent;

public abstract class AbstractController {

	@Autowired
	AccessControlService accessControl;

	@Autowired
	SessionCredentialsComponent sessionCredentials;

	@ModelAttribute("currentUser")
	public String getCurrentUser() {
		return accessControl.logIn(sessionCredentials.getLoginCredentials());
	}
}

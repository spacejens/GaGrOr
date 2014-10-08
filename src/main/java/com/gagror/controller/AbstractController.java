package com.gagror.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.view.RedirectView;

import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.service.accesscontrol.AccessControlService;

public abstract class AbstractController {

	@Autowired
	AccessControlService accessControl;

	@ModelAttribute("currentUser")
	public AccountReferenceOutput getCurrentUser() {
		return accessControl.getRequestAccount();
	}

	protected RedirectView redirect(final String url) {
		final RedirectView output = new RedirectView(url, true);
		output.setExposeModelAttributes(false);
		return output;
	}
}

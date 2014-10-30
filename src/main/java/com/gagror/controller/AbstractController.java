package com.gagror.controller;

import static com.gagror.data.account.SecurityRoles.IS_PUBLIC;
import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.view.RedirectView;

import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.service.accesscontrol.AccessControlService;

@CommonsLog
public abstract class AbstractController {

	@Autowired
	protected AccessControlService accessControl;

	@PreAuthorize(IS_PUBLIC)
	@ModelAttribute("currentUser")
	public AccountReferenceOutput getCurrentUser() {
		log.trace("Getting current user model attribute");
		return accessControl.getRequestAccount();
	}

	protected RedirectView redirect(final String url) {
		log.info(String.format("Redirecting to URL: %s", url));
		final RedirectView output = new RedirectView(url, true);
		output.setExposeModelAttributes(false);
		return output;
	}
}

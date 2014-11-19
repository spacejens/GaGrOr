package com.gagror.controller;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.view.RedirectView;

import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.account.SecurityRoles;
import com.gagror.service.accesscontrol.AccessControlService;

@CommonsLog
public abstract class AbstractController {

	protected static final String IS_LOGGED_IN = "isAuthenticated()";
	protected static final String IS_ADMIN = "hasRole('"+SecurityRoles.ROLE_ADMIN+"')";
	public static final String IS_PUBLIC = "true";
	protected static final String IS_NOT_LOGGED_IN = "isAnonymous()";

	protected static final String ATTR_ACCOUNT_ID = "accountId";
	protected static final String ATTR_GROUP_ID = "groupId";
	protected static final String MAY_VIEW_GROUP = IS_LOGGED_IN + " and hasPermission(#" + ATTR_GROUP_ID + ", 'viewGroup')"; // TODO Group view permission should not require being logged in
	protected static final String MAY_ADMIN_GROUP = IS_LOGGED_IN + " and hasPermission(#" + ATTR_GROUP_ID + ", 'adminGroup')";

	@Autowired
	protected AccessControlService accessControl;

	@PreAuthorize(IS_PUBLIC)
	@ModelAttribute("currentUser") // This annotation is OK here because every page needs this data to render the menu
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

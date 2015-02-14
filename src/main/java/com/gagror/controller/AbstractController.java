package com.gagror.controller;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.view.RedirectView;

import com.gagror.data.account.CurrentUserOutput;
import com.gagror.data.account.SecurityRoles;
import com.gagror.data.group.GroupIdentifiable;
import com.gagror.service.accesscontrol.AccessControlService;

@CommonsLog
public abstract class AbstractController {

	protected static final String IS_LOGGED_IN = "isAuthenticated()";
	protected static final String IS_ADMIN = "hasRole('"+SecurityRoles.ROLE_ADMIN+"')";
	protected static final String IS_PUBLIC = "true";
	protected static final String IS_NOT_LOGGED_IN = "isAnonymous()";

	protected static final String ATTR_ACCOUNT_ID = "accountId";
	protected static final String ATTR_GROUP_ID = "groupId";
	protected static final String MAY_VIEW_GROUP = "hasPermission(#" + ATTR_GROUP_ID + ", 'viewGroup')";
	protected static final String MAY_VIEW_GROUP_RULES = "hasPermission(#" + ATTR_GROUP_ID + ", 'viewGroupRules')";
	protected static final String MAY_ADMIN_GROUP = IS_LOGGED_IN + " and hasPermission(#" + ATTR_GROUP_ID + ", 'adminGroup')";

	@Autowired
	protected AccessControlService accessControl;

	@PreAuthorize(IS_PUBLIC)
	@ModelAttribute("currentUser") // This annotation is OK here because every page needs this data to render the menu
	public CurrentUserOutput getCurrentUser() {
		log.trace("Getting current user model attribute");
		return accessControl.getCurrentUser();
	}

	protected <F extends GroupIdentifiable> void verifyURLGroupIdMatchesForm(final Long urlGroupId, final F form) {
		if(! urlGroupId.equals(form.getGroupId())) {
			throw new FormAndURLMismatchException("Group ID", urlGroupId, form.getGroupId());
		}
	}

	// TODO Replace whitelabel error page with custom error page
	// http://stackoverflow.com/questions/25356781/spring-boot-remove-whitelabel-error-page
	// http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-error-handling

	protected RedirectView redirect(final String url) {
		log.info(String.format("Redirecting to URL: %s", url));
		final RedirectView output = new RedirectView(url, true);
		output.setExposeModelAttributes(false);
		return output;
	}
}

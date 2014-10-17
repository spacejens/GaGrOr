package com.gagror.controller;

import static com.gagror.data.account.SecurityRoles.IS_LOGGED_IN;
import lombok.extern.apachecommons.CommonsLog;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
@CommonsLog
public class AccountController extends AbstractController {

	@PreAuthorize(IS_LOGGED_IN + " and hasPermission(#accountId, 'editAccount')")
	@RequestMapping("/edit/{accountId}")
	public String editProfileForm(@PathVariable("accountId") final Long accountId) {
		log.info(String.format("Viewing edit account form for account ID %d", accountId));
		return "edit_account";
	}
}

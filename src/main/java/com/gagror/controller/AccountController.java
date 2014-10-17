package com.gagror.controller;

import static com.gagror.data.account.SecurityRoles.IS_LOGGED_IN;

import javax.validation.Valid;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gagror.data.account.AccountEditInput;
import com.gagror.data.account.AccountEditOutput;
import com.gagror.service.account.AccountService;

@Controller
@RequestMapping("/account")
@CommonsLog
public class AccountController extends AbstractController {

	@Autowired
	AccountService accountService;

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/contacts")
	public String contacts(final Model model) {
		log.info("Viewing contacts page");
		// TODO Place contacts as a separate ModelAttribute controller method
		model.addAttribute("contacts", accountService.loadContacts());
		return "contacts";
	}

	@PreAuthorize(IS_LOGGED_IN + " and hasPermission(#accountId, 'editAccount')")
	@RequestMapping(value="/edit/{accountId}", method=RequestMethod.GET)
	public String editUserForm(@PathVariable("accountId") final Long accountId, final Model model) {
		log.info(String.format("Viewing edit account form for account ID %d", accountId));
		final AccountEditOutput currentState = loadCurrentState(accountId);
		model.addAttribute("currentState", currentState);
		model.addAttribute("editAccountForm", new AccountEditInput(currentState));
		return "edit_account";
	}

	@PreAuthorize(IS_LOGGED_IN + " and hasPermission(#accountId, 'editAccount')")
	@RequestMapping(value="/edit/{accountId}", method=RequestMethod.POST)
	public Object saveUserForm(
			@PathVariable("accountId") final Long accountId,
			final Model model,
			@Valid @ModelAttribute("editAccountForm") final AccountEditInput editAccountForm,
			final BindingResult bindingResult) {
		if(! accountId.equals(editAccountForm.getId())) {
			log.error(String.format("Account ID URL (%d) and form (%d) mismatch when attempting to save user form", accountId, editAccountForm.getId()));
			throw new IllegalArgumentException(String.format("Unexpected account ID in user form"));
		}
		if(bindingResult.hasErrors()) {
			log.info(String.format("Failed to edit account ID %d, form had errors", accountId));
			final AccountEditOutput currentState = loadCurrentState(accountId);
			model.addAttribute("currentState", currentState);
			return "edit_account";
		}
		accountService.saveAccount(editAccountForm, bindingResult);
		if(bindingResult.hasErrors()) {
			log.info(String.format("Failed to edit account ID %d, rejected by service layer", accountId));
			final AccountEditOutput currentState = loadCurrentState(accountId);
			model.addAttribute("currentState", currentState);
			return "edit_account";
		}
		log.info(String.format("Saving edited account %s (ID %d)", editAccountForm.getUsername(), accountId));
		return redirect("/");
	}

	protected AccountEditOutput loadCurrentState(final Long accountId) {
		final AccountEditOutput currentState = accountService.loadAccountForEditing(accountId);
		if(null == currentState) {
			throw new IllegalArgumentException(String.format("Failed to find account ID %d for editing", accountId));
		}
		return currentState;
	}
}

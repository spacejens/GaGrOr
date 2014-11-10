package com.gagror.controller.system;

import static com.gagror.data.account.SecurityRoles.IS_LOGGED_IN;

import java.util.List;

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
import org.springframework.web.servlet.view.RedirectView;

import com.gagror.controller.AbstractController;
import com.gagror.data.account.AccountEditInput;
import com.gagror.data.account.AccountEditOutput;
import com.gagror.data.account.ContactReferenceOutput;
import com.gagror.service.social.AccountService;
import com.gagror.service.social.EditAccountPersister;

@Controller
@RequestMapping("/account")
@CommonsLog
public class AccountController extends AbstractController {

	@Autowired
	AccountService accountService;

	@Autowired
	EditAccountPersister editAccountPersister;

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/contacts")
	public String contacts(final Model model) {
		log.info("Viewing contact list page");
		return "contacts";
	}

	@PreAuthorize(IS_LOGGED_IN)
	@ModelAttribute("contacts")
	public List<ContactReferenceOutput> getContacts() {
		return accountService.loadContacts();
	}

	@PreAuthorize(IS_LOGGED_IN)
	@ModelAttribute("sentContactRequests")
	public List<ContactReferenceOutput> getSentContactRequests() {
		return accountService.loadSentContactRequests();
	}

	@PreAuthorize(IS_LOGGED_IN)
	@ModelAttribute("receivedContactRequests")
	public List<ContactReferenceOutput> getReceivedContactRequests() {
		return accountService.loadReceivedContactRequests();
	}

	// TODO Place the functionality for finding new contacts on a separate page

	@PreAuthorize(IS_LOGGED_IN)
	@ModelAttribute("notMyContacts")
	public List<ContactReferenceOutput> getNotMyContacts() {
		return accountService.loadAccountsNotContacts();
	}

	@PreAuthorize(IS_LOGGED_IN + " and hasPermission(#contactId, 'hasContact')")
	@RequestMapping("/viewcontact/{contactId}")
	public String viewContact(@PathVariable("contactId") final Long contactId, final Model model) {
		log.info(String.format("Viewing contact %d", contactId));
		model.addAttribute("contact", accountService.loadContact(contactId));
		return "view_contact";
	}

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/request/{accountId}")
	public RedirectView requestContact(@PathVariable("accountId") final Long accountId) {
		log.info(String.format("Sending contact request to account %d", accountId));
		accountService.createContactRequest(accountId);
		return redirect("/account/contacts");
	}

	@PreAuthorize(IS_LOGGED_IN + " and hasPermission(#contactId, 'hasContactRequest')")
	@RequestMapping("/accept/{contactId}")
	public RedirectView acceptContactRequest(@PathVariable("contactId") final Long contactId) {
		log.info(String.format("Accepting contact request %d", contactId));
		accountService.acceptContactRequest(contactId);
		return redirect("/account/contacts");
	}

	@PreAuthorize(IS_LOGGED_IN + " and hasPermission(#contactId, 'hasContactRequest')")
	@RequestMapping("/decline/{contactId}")
	public RedirectView declineContactRequest(@PathVariable("contactId") final Long contactId) {
		log.info(String.format("Declining contact request %d", contactId));
		accountService.declineContactRequest(contactId);
		return redirect("/account/contacts");
	}

	@PreAuthorize(IS_LOGGED_IN + " and hasPermission(#contactId, 'hasContact')")
	@RequestMapping("/delete/{contactId}")
	public RedirectView deleteContact(@PathVariable("contactId") final Long contactId) {
		log.info(String.format("Deleting contact %d", contactId));
		accountService.deleteContact(contactId);
		return redirect("/account/contacts");
	}

	@PreAuthorize(IS_LOGGED_IN + " and hasPermission(#accountId, 'editAccount')")
	@RequestMapping(value="/login/{accountId}", method=RequestMethod.GET)
	public RedirectView loginAsUser(@PathVariable("accountId") final Long accountId) {
		log.info(String.format("Logging in as user %d", accountId));
		accountService.loginAsUser(accountId);
		return redirect("/");
	}

	// TODO Split edit account page into separate tabs/forms (basic info, password)

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
		if(editAccountPersister.save(editAccountForm, bindingResult)) {
			log.info(String.format("Saving edited account %s (ID %d)", editAccountForm.getUsername(), accountId));
			return redirect("/");
		} else {
			log.warn(String.format("Failed to edit account ID %d", accountId));
			final AccountEditOutput currentState = loadCurrentState(accountId);
			model.addAttribute("currentState", currentState);
			return "edit_account";
		}
	}

	protected AccountEditOutput loadCurrentState(final Long accountId) {
		final AccountEditOutput currentState = accountService.loadAccountForEditing(accountId);
		if(null == currentState) {
			throw new IllegalArgumentException(String.format("Failed to find account ID %d for editing", accountId));
		}
		return currentState;
	}
}

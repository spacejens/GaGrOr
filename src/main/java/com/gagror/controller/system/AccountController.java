package com.gagror.controller.system;

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
import com.gagror.controller.FormAndURLMismatchException;
import com.gagror.data.account.AccountEditInput;
import com.gagror.data.account.AccountEditOutput;
import com.gagror.service.social.AccountService;
import com.gagror.service.social.EditAccountPersister;

@Controller
@RequestMapping("/account")
@CommonsLog
public class AccountController extends AbstractController {

	protected static final String ATTR_CONTACT_ID = "contactId";
	protected static final String HAS_CONTACT = IS_LOGGED_IN + " and hasPermission(#" + ATTR_CONTACT_ID + ", 'hasContact')";
	protected static final String HAS_INCOMING_CONTACT_REQUEST = IS_LOGGED_IN + " and hasPermission(#" + ATTR_CONTACT_ID + ", 'hasContactRequest')";
	protected static final String MAY_EDIT_ACCOUNT = IS_LOGGED_IN + " and hasPermission(#" + ATTR_ACCOUNT_ID + ", 'editAccount')";

	@Autowired
	AccountService accountService;

	@Autowired
	EditAccountPersister editAccountPersister;

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/contacts")
	public String contacts(final Model model) {
		log.info("Viewing contact list page");
		model.addAttribute("contacts", accountService.loadContacts());
		model.addAttribute("sentContactRequests", accountService.loadSentContactRequests());
		model.addAttribute("receivedContactRequests", accountService.loadReceivedContactRequests());
		return "contacts";
	}

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/contacts/find")
	public String findContacts(final Model model) {
		log.info("Viewing find contacts page");
		model.addAttribute("notMyContacts", accountService.loadAccountsNotContacts());
		return "find_contacts";
	}

	@PreAuthorize(HAS_CONTACT)
	@RequestMapping("/viewcontact/{" + ATTR_CONTACT_ID + "}")
	public String viewContact(@PathVariable(ATTR_CONTACT_ID) final Long contactId, final Model model) {
		log.info(String.format("Viewing contact %d", contactId));
		model.addAttribute("contact", accountService.loadContact(contactId));
		return "view_contact";
	}

	@PreAuthorize(IS_LOGGED_IN)
	@RequestMapping("/request/{" + ATTR_ACCOUNT_ID + "}")
	public RedirectView requestContact(@PathVariable(ATTR_ACCOUNT_ID) final Long accountId) {
		log.info(String.format("Sending contact request to account %d", accountId));
		accountService.createContactRequest(accountId);
		return redirect("/account/contacts");
	}

	@PreAuthorize(HAS_INCOMING_CONTACT_REQUEST)
	@RequestMapping("/accept/{" + ATTR_CONTACT_ID + "}")
	public RedirectView acceptContactRequest(@PathVariable(ATTR_CONTACT_ID) final Long contactId) {
		log.info(String.format("Accepting contact request %d", contactId));
		accountService.acceptContactRequest(contactId);
		return redirect("/account/contacts");
	}

	@PreAuthorize(HAS_INCOMING_CONTACT_REQUEST)
	@RequestMapping("/decline/{" + ATTR_CONTACT_ID + "}")
	public RedirectView declineContactRequest(@PathVariable(ATTR_CONTACT_ID) final Long contactId) {
		log.info(String.format("Declining contact request %d", contactId));
		accountService.declineContactRequest(contactId);
		return redirect("/account/contacts");
	}

	@PreAuthorize(HAS_CONTACT)
	@RequestMapping("/delete/{" + ATTR_CONTACT_ID + "}")
	public RedirectView deleteContact(@PathVariable("contactId") final Long contactId) {
		log.info(String.format("Deleting contact %d", contactId));
		accountService.deleteContact(contactId);
		return redirect("/account/contacts");
	}

	@PreAuthorize(MAY_EDIT_ACCOUNT)
	@RequestMapping(value="/login/{" + ATTR_ACCOUNT_ID + "}", method=RequestMethod.GET)
	public RedirectView loginAsUser(@PathVariable(ATTR_ACCOUNT_ID) final Long accountId) {
		log.info(String.format("Logging in as user %d", accountId));
		accountService.loginAsUser(accountId);
		return redirect("/");
	}

	@PreAuthorize(MAY_EDIT_ACCOUNT)
	@RequestMapping(value="/edit/{" + ATTR_ACCOUNT_ID + "}", method=RequestMethod.GET)
	public String editUserForm(@PathVariable(ATTR_ACCOUNT_ID) final Long accountId, final Model model) {
		log.info(String.format("Viewing edit account form for account ID %d", accountId));
		final AccountEditOutput currentState = accountService.loadAccountForEditing(accountId);
		model.addAttribute("currentState", currentState);
		model.addAttribute("editAccountForm", new AccountEditInput(currentState));
		return "edit_account";
	}

	@PreAuthorize(MAY_EDIT_ACCOUNT)
	@RequestMapping(value="/edit/{" + ATTR_ACCOUNT_ID + "}", method=RequestMethod.POST)
	public Object saveUserForm(
			@PathVariable(ATTR_ACCOUNT_ID) final Long accountId,
			final Model model,
			@Valid @ModelAttribute("editAccountForm") final AccountEditInput editAccountForm,
			final BindingResult bindingResult) {
		if(! editAccountForm.hasId(accountId)) {
			throw new FormAndURLMismatchException("Account ID", accountId, editAccountForm.getId());
		}
		if(editAccountPersister.save(editAccountForm, bindingResult)) {
			log.info(String.format("Saving edited account ID %d: %s", accountId, editAccountForm));
			return redirect("/");
		} else {
			log.warn(String.format("Failed to edit account ID %d", accountId));
			final AccountEditOutput currentState = accountService.loadAccountForEditing(accountId);
			model.addAttribute("currentState", currentState);
			return "edit_account";
		}
	}
}

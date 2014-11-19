package com.gagror.controller.system;

import javax.validation.Valid;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gagror.controller.AbstractController;
import com.gagror.data.account.RegisterInput;
import com.gagror.service.accesscontrol.RegisterAccountPersister;

@Controller
@RequestMapping("/access")
@CommonsLog
public class AccessController extends AbstractController {

	@Autowired
	RegisterAccountPersister registerAccountPersister;

	@PreAuthorize(IS_PUBLIC)
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loginForm() {
		log.info("Viewing login form");
		return "login";
	}

	@PreAuthorize(IS_NOT_LOGGED_IN)
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerForm(@ModelAttribute("registerForm") final RegisterInput registerForm) {
		log.info("Viewing register form");
		return "register";
	}

	@PreAuthorize(IS_NOT_LOGGED_IN)
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public Object registerProcess(
			@Valid @ModelAttribute("registerForm") final RegisterInput registerForm,
			final BindingResult bindingResult) {
		if(registerAccountPersister.save(registerForm, bindingResult)) {
			log.info(String.format("Successfully registered user '%s'", registerForm.getUsername()));
			return redirect("/");
		} else {
			log.warn(String.format("Failed to register user: %s", registerForm));
			return "register";
		}
	}
}

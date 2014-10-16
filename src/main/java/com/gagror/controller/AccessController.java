package com.gagror.controller;

import javax.validation.Valid;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gagror.data.account.RegisterInput;
import com.gagror.service.accesscontrol.AccessControlService;

@Controller
@RequestMapping("/access")
@CommonsLog
public class AccessController extends AbstractController {

	@Autowired
	AccessControlService accessControl;

	@PreAuthorize("true")
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loginForm() {
		log.info("Viewing login form");
		return "login";
	}

	@PreAuthorize("isAnonymous()")
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerForm(@ModelAttribute("registerForm") final RegisterInput registerForm) {
		log.info("Viewing register form");
		return "register";
	}

	@PreAuthorize("isAnonymous()")
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public Object registerProcess(
			@Valid @ModelAttribute("registerForm") final RegisterInput registerForm,
			final BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			log.info(String.format("Failed to register user '%s', form had errors", registerForm.getUsername()));
			return "register";
		}
		accessControl.register(registerForm, bindingResult);
		if(bindingResult.hasErrors()) {
			log.info(String.format("Failed to register user '%s', rejected by service layer", registerForm.getUsername()));
			return "register";
		}
		log.info(String.format("Successfully registered user '%s'", registerForm.getUsername()));
		return redirect("/");
	}
}

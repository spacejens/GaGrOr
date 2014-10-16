package com.gagror.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gagror.data.account.RegisterInput;
import com.gagror.service.accesscontrol.AccessControlService;

@Controller
@RequestMapping("/access")
public class AccessController extends AbstractController {

	@Autowired
	AccessControlService accessControl;

	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerForm(@ModelAttribute("registerForm") final RegisterInput registerForm) {
		return "register";
	}

	@RequestMapping(value="/register", method=RequestMethod.POST)
	public Object registerProcess(
			@Valid @ModelAttribute("registerForm") final RegisterInput registerForm,
			final BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "register";
		}
		accessControl.register(registerForm, bindingResult);
		if(bindingResult.hasErrors()) {
			return "register";
		}
		return redirect("/access/login");
	}
}

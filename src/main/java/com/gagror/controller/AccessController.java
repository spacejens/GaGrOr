package com.gagror.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import com.gagror.data.account.LoginCredentialsInput;
import com.gagror.data.account.RegisterInput;
import com.gagror.service.accesscontrol.AccessControlService;

@Controller
@RequestMapping("/access")
public class AccessController extends AbstractController {

	@Autowired
	AccessControlService accessControl;

	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loginForm(final Model model) {
		model.addAttribute("loginForm", new LoginCredentialsInput());
		return "login";
	}

	@RequestMapping(value="/login", method = RequestMethod.POST)
	public RedirectView loginProcess(final Model model, final LoginCredentialsInput loginForm) {
		switch(accessControl.logIn(loginForm)) {
		case LOGGED_IN:
			return redirect("/");
		default:
			// TODO Show appropriate form error when login fails
			return redirect("/access/login");
		}
	}

	@RequestMapping(value="/logout")
	public RedirectView logOut(final Model model) {
		accessControl.logOut();
		return redirect("/access/login");
	}

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerForm(final Model model) {
		model.addAttribute("registerForm", new RegisterInput());
		return "register";
	}

	@RequestMapping(value="/register", method=RequestMethod.POST)
	public RedirectView registerProcess(final Model model, final RegisterInput registerForm) {
		switch(accessControl.register(registerForm)) {
		case LOGGED_IN:
			return redirect("/");
		case LOGIN_FAILED:
			// TODO Unexpected immediate login failure, do something else here?
			return redirect("/access/login");
		case REGISTER_FAILED_PASSWORDS_DONT_MATCH:
		case REGISTER_FAILED_USERNAME_BUSY:
		default:
			// TODO Show appropriate form errors when registration fails
			return redirect("/access/register");
		}
	}
}

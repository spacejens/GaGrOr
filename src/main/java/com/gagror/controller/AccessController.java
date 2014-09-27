package com.gagror.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import com.gagror.data.account.LoginCredentialsInput;
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
		accessControl.logIn(loginForm);
		// TODO Redirect to different pages depending on result
		return redirect("/");
	}

	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public RedirectView logOut(final Model model) {
		accessControl.logOut();
		return redirect("/access/login");
	}

	@RequestMapping("/register")
	public String register(final Model model) {
		return "register";
	}
}
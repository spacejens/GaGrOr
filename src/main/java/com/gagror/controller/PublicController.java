package com.gagror.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PublicController extends AbstractController {

	@RequestMapping("/")
	public String about(final Model model) {
		return "about";
	}
}

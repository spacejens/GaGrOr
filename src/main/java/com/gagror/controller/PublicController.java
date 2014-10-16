package com.gagror.controller;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CommonsLog
public class PublicController extends AbstractController {

	@RequestMapping("/")
	public String about(final Model model) {
		log.info("Viewing about page");
		return "about";
	}
}

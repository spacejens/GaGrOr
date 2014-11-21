package com.gagror.controller.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gagror.controller.AbstractController;
import com.gagror.service.social.GroupService;

@Controller
@RequestMapping("/wh40kskirmish/rules")
@CommonsLog
public class Wh40kSkirmishRulesController extends AbstractController {

	@Autowired
	GroupService groupService;

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/view/{" + ATTR_GROUP_ID + "}")
	public String viewRules(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing rules for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		return "wh40kskirmish/rules_view";
	}
}

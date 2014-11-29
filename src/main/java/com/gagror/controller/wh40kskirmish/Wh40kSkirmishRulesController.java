package com.gagror.controller.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gagror.controller.AbstractController;
import com.gagror.service.social.GroupService;
import com.gagror.service.wh40kskirmish.Wh40kSkirmishRulesService;

@Controller
@RequestMapping("/wh40kskirmish/rules")
@CommonsLog
public class Wh40kSkirmishRulesController extends AbstractController {

	@Autowired
	GroupService groupService;

	@Autowired
	Wh40kSkirmishRulesService rulesService;

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/view/{" + ATTR_GROUP_ID + "}")
	public String viewRules(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing rules for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("rules", rulesService.viewRules(groupId));
		// TODO Make initial territory allocation for gangs configurable
		// TODO Make initial money per gang configurable
		// TODO Make name of currency in game configurable
		return "wh40kskirmish/rules_view";
	}

	// TODO Add page to edit basic rules

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/gangtypes/list/{" + ATTR_GROUP_ID + "}")
	public String listGangTypes(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing gang types for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("rules", rulesService.viewRulesWithGangTypes(groupId));
		return "wh40kskirmish/gangtypes_list";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/gangtypes/create/{" + ATTR_GROUP_ID + "}", method=RequestMethod.GET)
	public String createGangTypeForm(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing create gang type form for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("rules", rulesService.viewRules(groupId));
		// TODO Add HTML form to create (or edit) gang types
		return "wh40kskirmish/gangtypes_edit";
	}

	// TODO Add logic to handle posting of create gang type form

	// TODO Add page to view single gang type

	// TODO Add page to edit gang types

	// TODO Add page to view skills

	// TODO Add page to edit skills

	// TODO Add page to view equipment

	// TODO Add page to edit equipment

	// TODO Add page to view territories

	// TODO Add page to edit territories
}

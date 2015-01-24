package com.gagror.controller.wh40kskirmish;

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

import com.gagror.controller.AbstractController;
import com.gagror.controller.FormAndURLMismatchException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesInput;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFactionInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFactionOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceOutput;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryInput;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemTypeInput;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemTypeOutput;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryInput;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillInput;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillOutput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryInput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryTypeInput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryTypeOutput;
import com.gagror.service.social.GroupService;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishFactionPersister;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishFighterTypePersister;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishGangTypePersister;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishItemCategoryPersister;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishItemTypePersister;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishRacePersister;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishRulesPersister;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishRulesService;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishSkillCategoryPersister;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishSkillPersister;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishTerritoryCategoryPersister;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishTerritoryTypePersister;

@Controller
@RequestMapping("/wh40kskirmish/rules")
@CommonsLog
public class Wh40kSkirmishRulesController extends AbstractController {

	protected static final String ATTR_GANGTYPE_ID = "gangTypeId";
	protected static final String ATTR_FACTION_ID = "factionId";
	protected static final String ATTR_RACE_ID = "raceId";
	protected static final String ATTR_FIGHTERTYPE_ID = "fighterTypeId";
	protected static final String ATTR_TERRITORYCATEGORY_ID = "territoryCategoryId";
	protected static final String ATTR_TERRITORYTYPE_ID = "territoryTypeId";
	protected static final String ATTR_SKILLCATEGORY_ID = "skillCategoryId";
	protected static final String ATTR_SKILL_ID = "skillId";
	protected static final String ATTR_ITEMCATEGORY_ID = "itemCategoryId";
	protected static final String ATTR_ITEMTYPE_ID = "itemTypeId";

	@Autowired
	GroupService groupService;

	@Autowired
	Wh40kSkirmishRulesService rulesService;

	@Autowired
	Wh40kSkirmishRulesPersister rulesPersister;

	@Autowired
	Wh40kSkirmishGangTypePersister gangTypePersister;

	@Autowired
	Wh40kSkirmishFactionPersister factionPersister;

	@Autowired
	Wh40kSkirmishRacePersister racePersister;

	@Autowired
	Wh40kSkirmishFighterTypePersister fighterTypePersister;

	@Autowired
	Wh40kSkirmishTerritoryCategoryPersister territoryCategoryPersister;

	@Autowired
	Wh40kSkirmishTerritoryTypePersister territoryTypePersister;

	@Autowired
	Wh40kSkirmishSkillCategoryPersister skillCategoryPersister;

	@Autowired
	Wh40kSkirmishSkillPersister skillPersister;

	@Autowired
	Wh40kSkirmishItemCategoryPersister itemCategoryPersister;

	@Autowired
	Wh40kSkirmishItemTypePersister itemTypePersister;

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}")
	public String viewRules(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing rules for group %d", groupId));
		model.addAttribute("rules", rulesService.viewRulesListChildren(groupId));
		return "wh40kskirmish/rules_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/edit", method=RequestMethod.GET)
	public String editRulesForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model) {
		log.info(String.format("Viewing edit rules form for group %d", groupId));
		final Wh40kSkirmishRulesOutput rules = rulesService.viewRules(groupId);
		model.addAttribute("group", rules.getGroup());
		model.addAttribute("rulesForm", new Wh40kSkirmishRulesInput(rules));
		return "wh40kskirmish/rules_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/save", method=RequestMethod.POST)
	public Object saveRulesForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("rulesForm") final Wh40kSkirmishRulesInput rulesForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(rulesForm.getGroupId())) {
			throw new FormAndURLMismatchException("Group ID", groupId, rulesForm.getGroupId());
		}
		if(rulesPersister.save(rulesForm, bindingResult)) {
			log.info(String.format("Saved rules: %s", rulesForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", rulesForm));
			model.addAttribute("group", groupService.viewGroup(groupId));
			return "wh40kskirmish/rules_edit";
		}
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/gangtype/create", method=RequestMethod.GET)
	public String createGangTypeForm(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing create gang type form for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("gangTypeForm", new Wh40kSkirmishGangTypeInput(groupId));
		return "wh40kskirmish/gangtypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/gangtype/save", method=RequestMethod.POST)
	public Object saveGangTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("gangTypeForm") final Wh40kSkirmishGangTypeInput gangTypeForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(gangTypeForm.getGroupId())) {
			throw new FormAndURLMismatchException("Group ID", groupId, gangTypeForm.getGroupId());
		}
		if(gangTypePersister.save(gangTypeForm, bindingResult)) {
			log.info(String.format("Saved gang type: %s", gangTypeForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", gangTypeForm));
			model.addAttribute("group", groupService.viewGroup(groupId));
			return "wh40kskirmish/gangtypes_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/gangtype/{" + ATTR_GANGTYPE_ID + "}")
	public String viewGangType(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			final Model model) {
		model.addAttribute("gangType", rulesService.viewGangType(groupId, gangTypeId));
		return "wh40kskirmish/gangtypes_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/gangtype/{" + ATTR_GANGTYPE_ID + "}/edit", method=RequestMethod.GET)
	public String editGangTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			final Model model) {
		log.info(String.format("Viewing edit gang type form for gang type %d in group %d", gangTypeId, groupId));
		final Wh40kSkirmishGangTypeOutput gangType = rulesService.viewGangType(groupId, gangTypeId);
		model.addAttribute("group", gangType.getGroup());
		model.addAttribute("gangTypeForm", new Wh40kSkirmishGangTypeInput(gangType));
		return "wh40kskirmish/gangtypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/faction/{" + ATTR_GANGTYPE_ID + "}/create", method=RequestMethod.GET)
	public String createFactionForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			final Model model) {
		log.info(String.format("Viewing create faction form for gang type %d of group %d", gangTypeId, groupId));
		model.addAttribute("gangType", rulesService.viewGangType(groupId, gangTypeId));
		model.addAttribute("factionForm", new Wh40kSkirmishFactionInput(groupId, gangTypeId));
		return "wh40kskirmish/factions_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/faction/save", method=RequestMethod.POST)
	public Object saveFactionForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("factionForm") final Wh40kSkirmishFactionInput factionForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(factionForm.getGroupId())) {
			throw new FormAndURLMismatchException("Group ID", groupId, factionForm.getGroupId());
		}
		if(factionPersister.save(factionForm, bindingResult)) {
			log.info(String.format("Saved faction: %s", factionForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", factionForm));
			model.addAttribute("gangType", rulesService.viewGangType(groupId, factionForm.getGangTypeId()));
			return "wh40kskirmish/factions_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/faction/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_FACTION_ID + "}")
	public String viewFaction(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_FACTION_ID) final Long factionId,
			final Model model) {
		model.addAttribute("faction", rulesService.viewFaction(groupId, gangTypeId, factionId));
		return "wh40kskirmish/factions_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/faction/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_FACTION_ID + "}/edit", method=RequestMethod.GET)
	public String editFactionForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_FACTION_ID) final Long factionId,
			final Model model) {
		log.info(String.format("Viewing edit faction form for faction %d of gang type %d in group %d", factionId, gangTypeId, groupId));
		final Wh40kSkirmishFactionOutput faction = rulesService.viewFaction(groupId, gangTypeId, factionId);
		model.addAttribute("gangType", faction.getGangType());
		model.addAttribute("factionForm", new Wh40kSkirmishFactionInput(faction));
		return "wh40kskirmish/factions_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/race/{" + ATTR_GANGTYPE_ID + "}/create", method=RequestMethod.GET)
	public String createRaceForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			final Model model) {
		log.info(String.format("Viewing create race form for gang type %d of group %d", gangTypeId, groupId));
		model.addAttribute("gangType", rulesService.viewGangType(groupId, gangTypeId));
		model.addAttribute("raceForm", new Wh40kSkirmishRaceInput(groupId, gangTypeId));
		return "wh40kskirmish/races_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/race/save", method=RequestMethod.POST)
	public Object saveRaceForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("raceForm") final Wh40kSkirmishRaceInput raceForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(raceForm.getGroupId())) {
			throw new FormAndURLMismatchException("Group ID", groupId, raceForm.getGroupId());
		}
		if(racePersister.save(raceForm, bindingResult)) {
			log.info(String.format("Saved race: %s", raceForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", raceForm));
			model.addAttribute("gangType", rulesService.viewGangType(groupId, raceForm.getGangTypeId()));
			return "wh40kskirmish/races_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/race/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_RACE_ID + "}")
	public String viewRace(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_RACE_ID) final Long raceId,
			final Model model) {
		model.addAttribute("race", rulesService.viewRace(groupId, gangTypeId, raceId));
		return "wh40kskirmish/races_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/race/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_RACE_ID + "}/edit", method=RequestMethod.GET)
	public String editRaceForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_RACE_ID) final Long raceId,
			final Model model) {
		log.info(String.format("Viewing edit race form for race %d of gang type %d in group %d", raceId, gangTypeId, groupId));
		final Wh40kSkirmishRaceOutput race = rulesService.viewRace(groupId, gangTypeId, raceId);
		model.addAttribute("gangType", race.getGangType());
		model.addAttribute("raceForm", new Wh40kSkirmishRaceInput(race));
		return "wh40kskirmish/races_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/fightertype/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_RACE_ID + "}/create", method=RequestMethod.GET)
	public String createFighterTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_RACE_ID) final Long raceId,
			final Model model) {
		log.info(String.format("Viewing create fighter type form for race %d of gang type %d of group %d", raceId, gangTypeId, groupId));
		model.addAttribute("race", rulesService.viewRace(groupId, gangTypeId, raceId));
		model.addAttribute("fighterTypeForm", new Wh40kSkirmishFighterTypeInput(groupId, gangTypeId, raceId));
		return "wh40kskirmish/fightertypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/fightertype/save", method=RequestMethod.POST)
	public Object saveFighterTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("fighterTypeForm") final Wh40kSkirmishFighterTypeInput fighterTypeForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(fighterTypeForm.getGroupId())) {
			throw new FormAndURLMismatchException("Group ID", groupId, fighterTypeForm.getGroupId());
		}
		if(fighterTypePersister.save(fighterTypeForm, bindingResult)) {
			log.info(String.format("Saved fighter type: %s", fighterTypeForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", fighterTypeForm));
			model.addAttribute("race", rulesService.viewRace(groupId, fighterTypeForm.getGangTypeId(), fighterTypeForm.getRaceId()));
			return "wh40kskirmish/fightertypes_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/fightertype/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_RACE_ID + "}/{" + ATTR_FIGHTERTYPE_ID + "}")
	public String viewFighterType(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_RACE_ID) final Long raceId,
			@PathVariable(ATTR_FIGHTERTYPE_ID) final Long fighterTypeId,
			final Model model) {
		model.addAttribute("fighterType", rulesService.viewFighterType(groupId, gangTypeId, raceId, fighterTypeId));
		return "wh40kskirmish/fightertypes_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/fightertype/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_RACE_ID + "}/{" + ATTR_FIGHTERTYPE_ID + "}/edit", method=RequestMethod.GET)
	public String editFighterTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_RACE_ID) final Long raceId,
			@PathVariable(ATTR_FIGHTERTYPE_ID) final Long fighterTypeId,
			final Model model) {
		log.info(String.format("Viewing edit fighter type form for fighter type %d of race %d of gang type %d in group %d", fighterTypeId, raceId, gangTypeId, groupId));
		final Wh40kSkirmishFighterTypeOutput fighterType = rulesService.viewFighterType(groupId, gangTypeId, raceId, fighterTypeId);
		model.addAttribute("race", fighterType.getRace());
		model.addAttribute("fighterTypeForm", new Wh40kSkirmishFighterTypeInput(fighterType));
		return "wh40kskirmish/fightertypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorycategory/create", method=RequestMethod.GET)
	public String createTerritoryCategoryForm(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing create territory category form for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("territoryCategoryForm", new Wh40kSkirmishTerritoryCategoryInput(groupId));
		return "wh40kskirmish/territorycategories_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorycategory/save", method=RequestMethod.POST)
	public Object saveTerritoryCategoryForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("territoryCategoryForm") final Wh40kSkirmishTerritoryCategoryInput territoryCategoryForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(territoryCategoryForm.getGroupId())) {
			throw new FormAndURLMismatchException("Group ID", groupId, territoryCategoryForm.getGroupId());
		}
		if(territoryCategoryPersister.save(territoryCategoryForm, bindingResult)) {
			log.info(String.format("Saved territory category: %s", territoryCategoryForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", territoryCategoryForm));
			model.addAttribute("group", groupService.viewGroup(groupId));
			return "wh40kskirmish/territorycategories_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/territorycategory/{" + ATTR_TERRITORYCATEGORY_ID + "}")
	public String viewTerritoryCategory(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_TERRITORYCATEGORY_ID) final Long territoryCategoryId,
			final Model model) {
		model.addAttribute("territoryCategory", rulesService.viewTerritoryCategory(groupId, territoryCategoryId));
		return "wh40kskirmish/territorycategories_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorycategory/{" + ATTR_TERRITORYCATEGORY_ID + "}/edit", method=RequestMethod.GET)
	public String editTerritoryCategoryForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_TERRITORYCATEGORY_ID) final Long territoryCategoryId,
			final Model model) {
		log.info(String.format("Viewing edit territory category form for territory category %d in group %d", territoryCategoryId, groupId));
		final Wh40kSkirmishTerritoryCategoryOutput territoryCategory = rulesService.viewTerritoryCategory(groupId, territoryCategoryId);
		model.addAttribute("group", territoryCategory.getGroup());
		model.addAttribute("territoryCategoryForm", new Wh40kSkirmishTerritoryCategoryInput(territoryCategory));
		return "wh40kskirmish/territorycategories_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorytype/{" + ATTR_TERRITORYCATEGORY_ID + "}/create", method=RequestMethod.GET)
	public String createTerritoryTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_TERRITORYCATEGORY_ID) final Long territoryCategoryId,
			final Model model) {
		log.info(String.format("Viewing create territory type form for territory category %d of group %d", territoryCategoryId, groupId));
		model.addAttribute("territoryCategory", rulesService.viewTerritoryCategory(groupId, territoryCategoryId));
		model.addAttribute("territoryTypeForm", new Wh40kSkirmishTerritoryTypeInput(groupId, territoryCategoryId));
		return "wh40kskirmish/territorytypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorytype/save", method=RequestMethod.POST)
	public Object saveTerritoryTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("territoryTypeForm") final Wh40kSkirmishTerritoryTypeInput territoryTypeForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(territoryTypeForm.getGroupId())) {
			throw new FormAndURLMismatchException("Group ID", groupId, territoryTypeForm.getGroupId());
		}
		if(territoryTypePersister.save(territoryTypeForm, bindingResult)) {
			log.info(String.format("Saved territory type: %s", territoryTypeForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", territoryTypeForm));
			model.addAttribute("territoryCategory", rulesService.viewTerritoryCategory(groupId, territoryTypeForm.getTerritoryCategoryId()));
			return "wh40kskirmish/territorytypes_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/territorytype/{" + ATTR_TERRITORYCATEGORY_ID + "}/{" + ATTR_TERRITORYTYPE_ID + "}")
	public String viewTerritoryType(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_TERRITORYCATEGORY_ID) final Long territoryCategoryId,
			@PathVariable(ATTR_TERRITORYTYPE_ID) final Long territoryTypeId,
			final Model model) {
		model.addAttribute("territoryType", rulesService.viewTerritoryType(groupId, territoryCategoryId, territoryTypeId));
		return "wh40kskirmish/territorytypes_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorytype/{" + ATTR_TERRITORYCATEGORY_ID + "}/{" + ATTR_TERRITORYTYPE_ID + "}/edit", method=RequestMethod.GET)
	public String editTerritoryTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_TERRITORYCATEGORY_ID) final Long territoryCategoryId,
			@PathVariable(ATTR_TERRITORYTYPE_ID) final Long territoryTypeId,
			final Model model) {
		log.info(String.format("Viewing edit territory type form for territory type %d of territory category %d in group %d", territoryTypeId, territoryCategoryId, groupId));
		final Wh40kSkirmishTerritoryTypeOutput territoryType = rulesService.viewTerritoryType(groupId, territoryCategoryId, territoryTypeId);
		model.addAttribute("territoryCategory", territoryType.getTerritoryCategory());
		model.addAttribute("territoryTypeForm", new Wh40kSkirmishTerritoryTypeInput(territoryType));
		return "wh40kskirmish/territorytypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/skillcategory/create", method=RequestMethod.GET)
	public String createSkillCategoryForm(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing create skill category form for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("skillCategoryForm", new Wh40kSkirmishSkillCategoryInput(groupId));
		return "wh40kskirmish/skillcategories_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/skillcategory/save", method=RequestMethod.POST)
	public Object saveSkillCategoryForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("skillCategoryForm") final Wh40kSkirmishSkillCategoryInput skillCategoryForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(skillCategoryForm.getGroupId())) {
			throw new FormAndURLMismatchException("Group ID", groupId, skillCategoryForm.getGroupId());
		}
		if(skillCategoryPersister.save(skillCategoryForm, bindingResult)) {
			log.info(String.format("Saved skill category: %s", skillCategoryForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", skillCategoryForm));
			model.addAttribute("group", groupService.viewGroup(groupId));
			return "wh40kskirmish/skillcategories_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/skillcategory/{" + ATTR_SKILLCATEGORY_ID + "}")
	public String viewSkillCategory(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_SKILLCATEGORY_ID) final Long skillCategoryId,
			final Model model) {
		model.addAttribute("skillCategory", rulesService.viewSkillCategory(groupId, skillCategoryId));
		return "wh40kskirmish/skillcategories_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/skillcategory/{" + ATTR_SKILLCATEGORY_ID + "}/edit", method=RequestMethod.GET)
	public String editSkillCategoryForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_SKILLCATEGORY_ID) final Long skillCategoryId,
			final Model model) {
		log.info(String.format("Viewing edit skill category form for skill category %d in group %d", skillCategoryId, groupId));
		final Wh40kSkirmishSkillCategoryOutput skillCategory = rulesService.viewSkillCategory(groupId, skillCategoryId);
		model.addAttribute("group", skillCategory.getGroup());
		model.addAttribute("skillCategoryForm", new Wh40kSkirmishSkillCategoryInput(skillCategory));
		return "wh40kskirmish/skillcategories_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/skill/{" + ATTR_SKILLCATEGORY_ID + "}/create", method=RequestMethod.GET)
	public String createSkillForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_SKILLCATEGORY_ID) final Long skillCategoryId,
			final Model model) {
		log.info(String.format("Viewing create skill form for skill category %d of group %d", skillCategoryId, groupId));
		model.addAttribute("skillCategory", rulesService.viewSkillCategory(groupId, skillCategoryId));
		model.addAttribute("skillForm", new Wh40kSkirmishSkillInput(groupId, skillCategoryId));
		return "wh40kskirmish/skills_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/skill/save", method=RequestMethod.POST)
	public Object saveSkillForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("skillForm") final Wh40kSkirmishSkillInput skillForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(skillForm.getGroupId())) {
			throw new FormAndURLMismatchException("Group ID", groupId, skillForm.getGroupId());
		}
		if(skillPersister.save(skillForm, bindingResult)) {
			log.info(String.format("Saved skill: %s", skillForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", skillForm));
			model.addAttribute("skillCategory", rulesService.viewSkillCategory(groupId, skillForm.getSkillCategoryId()));
			return "wh40kskirmish/skills_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/skill/{" + ATTR_SKILLCATEGORY_ID + "}/{" + ATTR_SKILL_ID + "}")
	public String viewSkill(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_SKILLCATEGORY_ID) final Long skillCategoryId,
			@PathVariable(ATTR_SKILL_ID) final Long skillId,
			final Model model) {
		model.addAttribute("skill", rulesService.viewSkill(groupId, skillCategoryId, skillId));
		return "wh40kskirmish/skills_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/skill/{" + ATTR_SKILLCATEGORY_ID + "}/{" + ATTR_SKILL_ID + "}/edit", method=RequestMethod.GET)
	public String editSkillForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_SKILLCATEGORY_ID) final Long skillCategoryId,
			@PathVariable(ATTR_SKILL_ID) final Long skillId,
			final Model model) {
		log.info(String.format("Viewing edit skill form for skill %d of skill category %d in group %d", skillId, skillCategoryId, groupId));
		final Wh40kSkirmishSkillOutput skill = rulesService.viewSkill(groupId, skillCategoryId, skillId);
		model.addAttribute("skillCategory", skill.getSkillCategory());
		model.addAttribute("skillForm", new Wh40kSkirmishSkillInput(skill));
		return "wh40kskirmish/skills_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/itemcategory/create", method=RequestMethod.GET)
	public String createItemCategoryForm(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing create item category form for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("itemCategoryForm", new Wh40kSkirmishItemCategoryInput(groupId));
		return "wh40kskirmish/itemcategories_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/itemcategory/save", method=RequestMethod.POST)
	public Object saveItemCategoryForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("itemCategoryForm") final Wh40kSkirmishItemCategoryInput itemCategoryForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(itemCategoryForm.getGroupId())) {
			throw new FormAndURLMismatchException("Group ID", groupId, itemCategoryForm.getGroupId());
		}
		if(itemCategoryPersister.save(itemCategoryForm, bindingResult)) {
			log.info(String.format("Saved item category: %s", itemCategoryForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", itemCategoryForm));
			model.addAttribute("group", groupService.viewGroup(groupId));
			return "wh40kskirmish/itemcategories_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/itemcategory/{" + ATTR_ITEMCATEGORY_ID + "}")
	public String viewItemCategory(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_ITEMCATEGORY_ID) final Long itemCategoryId,
			final Model model) {
		model.addAttribute("itemCategory", rulesService.viewItemCategory(groupId, itemCategoryId));
		return "wh40kskirmish/itemcategories_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/itemcategory/{" + ATTR_ITEMCATEGORY_ID + "}/edit", method=RequestMethod.GET)
	public String editItemCategoryForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_ITEMCATEGORY_ID) final Long itemCategoryId,
			final Model model) {
		log.info(String.format("Viewing edit item category form for item category %d in group %d", itemCategoryId, groupId));
		final Wh40kSkirmishItemCategoryOutput itemCategory = rulesService.viewItemCategory(groupId, itemCategoryId);
		model.addAttribute("group", itemCategory.getGroup());
		model.addAttribute("itemCategoryForm", new Wh40kSkirmishItemCategoryInput(itemCategory));
		return "wh40kskirmish/itemcategories_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/itemtype/{" + ATTR_ITEMCATEGORY_ID + "}/create", method=RequestMethod.GET)
	public String createItemTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_ITEMCATEGORY_ID) final Long itemCategoryId,
			final Model model) {
		log.info(String.format("Viewing create item type form for item category %d of group %d", itemCategoryId, groupId));
		model.addAttribute("itemCategory", rulesService.viewItemCategory(groupId, itemCategoryId));
		model.addAttribute("itemTypeForm", new Wh40kSkirmishItemTypeInput(groupId, itemCategoryId));
		return "wh40kskirmish/itemtypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/itemtype/save", method=RequestMethod.POST)
	public Object saveItemTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("itemTypeForm") final Wh40kSkirmishItemTypeInput itemTypeForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(itemTypeForm.getGroupId())) {
			throw new FormAndURLMismatchException("Group ID", groupId, itemTypeForm.getGroupId());
		}
		if(itemTypePersister.save(itemTypeForm, bindingResult)) {
			log.info(String.format("Saved item type: %s", itemTypeForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", itemTypeForm));
			model.addAttribute("itemCategory", rulesService.viewItemCategory(groupId, itemTypeForm.getItemCategoryId()));
			return "wh40kskirmish/itemtypes_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/itemtype/{" + ATTR_ITEMCATEGORY_ID + "}/{" + ATTR_ITEMTYPE_ID + "}")
	public String viewItemType(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_ITEMCATEGORY_ID) final Long itemCategoryId,
			@PathVariable(ATTR_ITEMTYPE_ID) final Long itemTypeId,
			final Model model) {
		model.addAttribute("itemType", rulesService.viewItemType(groupId, itemCategoryId, itemTypeId));
		return "wh40kskirmish/itemtypes_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/itemtype/{" + ATTR_ITEMCATEGORY_ID + "}/{" + ATTR_ITEMTYPE_ID + "}/edit", method=RequestMethod.GET)
	public String editItemTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_ITEMCATEGORY_ID) final Long itemCategoryId,
			@PathVariable(ATTR_ITEMTYPE_ID) final Long itemTypeId,
			final Model model) {
		log.info(String.format("Viewing edit item type form for item type %d of item category %d in group %d", itemTypeId, itemCategoryId, groupId));
		final Wh40kSkirmishItemTypeOutput itemType = rulesService.viewItemType(groupId, itemCategoryId, itemTypeId);
		model.addAttribute("itemCategory", itemType.getItemCategory());
		model.addAttribute("itemTypeForm", new Wh40kSkirmishItemTypeInput(itemType));
		return "wh40kskirmish/itemtypes_edit";
	}
}

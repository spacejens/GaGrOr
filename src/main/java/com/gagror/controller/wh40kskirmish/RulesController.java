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
import com.gagror.data.wh40kskirmish.rules.RulesInput;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionInput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceInput;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceOutput;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryInput;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeInput;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeOutput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryInput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillInput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillOutput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryInput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeInput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeOutput;
import com.gagror.service.social.GroupService;
import com.gagror.service.wh40kskirmish.rules.FactionPersister;
import com.gagror.service.wh40kskirmish.rules.FighterTypePersister;
import com.gagror.service.wh40kskirmish.rules.GangTypePersister;
import com.gagror.service.wh40kskirmish.rules.ItemCategoryPersister;
import com.gagror.service.wh40kskirmish.rules.ItemTypePersister;
import com.gagror.service.wh40kskirmish.rules.RacePersister;
import com.gagror.service.wh40kskirmish.rules.RulesPersister;
import com.gagror.service.wh40kskirmish.rules.RulesService;
import com.gagror.service.wh40kskirmish.rules.SkillCategoryPersister;
import com.gagror.service.wh40kskirmish.rules.SkillPersister;
import com.gagror.service.wh40kskirmish.rules.TerritoryCategoryPersister;
import com.gagror.service.wh40kskirmish.rules.TerritoryTypePersister;

@Controller
@RequestMapping("/wh40kskirmish/rules")
@CommonsLog
public class RulesController extends AbstractController {

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
	RulesService rulesService;

	@Autowired
	RulesPersister rulesPersister;

	@Autowired
	GangTypePersister gangTypePersister;

	@Autowired
	FactionPersister factionPersister;

	@Autowired
	RacePersister racePersister;

	@Autowired
	FighterTypePersister fighterTypePersister;

	@Autowired
	TerritoryCategoryPersister territoryCategoryPersister;

	@Autowired
	TerritoryTypePersister territoryTypePersister;

	@Autowired
	SkillCategoryPersister skillCategoryPersister;

	@Autowired
	SkillPersister skillPersister;

	@Autowired
	ItemCategoryPersister itemCategoryPersister;

	@Autowired
	ItemTypePersister itemTypePersister;

	@PreAuthorize(MAY_VIEW_GROUP_RULES)
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
		final RulesOutput rules = rulesService.viewRules(groupId);
		model.addAttribute("group", rules.getGroup());
		model.addAttribute("rulesForm", new RulesInput(rules));
		return "wh40kskirmish/rules_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/save", method=RequestMethod.POST)
	public Object saveRulesForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("rulesForm") final RulesInput rulesForm,
			final BindingResult bindingResult) {
		verifyURLGroupIdMatchesForm(groupId, rulesForm);
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
		model.addAttribute("gangTypeForm", new GangTypeInput(groupId));
		return "wh40kskirmish/gangtypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/gangtype/save", method=RequestMethod.POST)
	public Object saveGangTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("gangTypeForm") final GangTypeInput gangTypeForm,
			final BindingResult bindingResult) {
		verifyURLGroupIdMatchesForm(groupId, gangTypeForm);
		if(gangTypePersister.save(gangTypeForm, bindingResult)) {
			log.info(String.format("Saved gang type: %s", gangTypeForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", gangTypeForm));
			model.addAttribute("group", groupService.viewGroup(groupId));
			return "wh40kskirmish/gangtypes_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP_RULES)
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
		final GangTypeOutput gangType = rulesService.viewGangType(groupId, gangTypeId);
		model.addAttribute("group", gangType.getGroup());
		model.addAttribute("gangTypeForm", new GangTypeInput(gangType));
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
		model.addAttribute("factionForm", new FactionInput(groupId, gangTypeId));
		return "wh40kskirmish/factions_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/faction/save", method=RequestMethod.POST)
	public Object saveFactionForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("factionForm") final FactionInput factionForm,
			final BindingResult bindingResult) {
		verifyURLGroupIdMatchesForm(groupId, factionForm);
		if(factionPersister.save(factionForm, bindingResult)) {
			log.info(String.format("Saved faction: %s", factionForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", factionForm));
			model.addAttribute("gangType", rulesService.viewGangType(groupId, factionForm.getGangTypeId()));
			return "wh40kskirmish/factions_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP_RULES)
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
		final FactionOutput faction = rulesService.viewFaction(groupId, gangTypeId, factionId);
		model.addAttribute("gangType", faction.getGangType());
		model.addAttribute("factionForm", new FactionInput(faction));
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
		model.addAttribute("raceForm", new RaceInput(groupId, gangTypeId));
		return "wh40kskirmish/races_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/race/save", method=RequestMethod.POST)
	public Object saveRaceForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("raceForm") final RaceInput raceForm,
			final BindingResult bindingResult) {
		verifyURLGroupIdMatchesForm(groupId, raceForm);
		if(racePersister.save(raceForm, bindingResult)) {
			log.info(String.format("Saved race: %s", raceForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", raceForm));
			model.addAttribute("gangType", rulesService.viewGangType(groupId, raceForm.getGangTypeId()));
			return "wh40kskirmish/races_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP_RULES)
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
		final RaceOutput race = rulesService.viewRace(groupId, gangTypeId, raceId);
		model.addAttribute("gangType", race.getGangType());
		model.addAttribute("raceForm", new RaceInput(race));
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
		model.addAttribute("fighterTypeForm", new FighterTypeInput(groupId, gangTypeId, raceId));
		return "wh40kskirmish/fightertypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/fightertype/save", method=RequestMethod.POST)
	public Object saveFighterTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("fighterTypeForm") final FighterTypeInput fighterTypeForm,
			final BindingResult bindingResult) {
		verifyURLGroupIdMatchesForm(groupId, fighterTypeForm);
		if(fighterTypePersister.save(fighterTypeForm, bindingResult)) {
			log.info(String.format("Saved fighter type: %s", fighterTypeForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", fighterTypeForm));
			model.addAttribute("race", rulesService.viewRace(groupId, fighterTypeForm.getGangTypeId(), fighterTypeForm.getRaceId()));
			return "wh40kskirmish/fightertypes_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP_RULES)
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
		final FighterTypeOutput fighterType = rulesService.viewFighterType(groupId, gangTypeId, raceId, fighterTypeId);
		model.addAttribute("race", fighterType.getRace());
		model.addAttribute("fighterTypeForm", new FighterTypeInput(fighterType));
		return "wh40kskirmish/fightertypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorycategory/create", method=RequestMethod.GET)
	public String createTerritoryCategoryForm(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing create territory category form for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("territoryCategoryForm", new TerritoryCategoryInput(groupId));
		return "wh40kskirmish/territorycategories_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorycategory/save", method=RequestMethod.POST)
	public Object saveTerritoryCategoryForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("territoryCategoryForm") final TerritoryCategoryInput territoryCategoryForm,
			final BindingResult bindingResult) {
		verifyURLGroupIdMatchesForm(groupId, territoryCategoryForm);
		if(territoryCategoryPersister.save(territoryCategoryForm, bindingResult)) {
			log.info(String.format("Saved territory category: %s", territoryCategoryForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", territoryCategoryForm));
			model.addAttribute("group", groupService.viewGroup(groupId));
			return "wh40kskirmish/territorycategories_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP_RULES)
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
		final TerritoryCategoryOutput territoryCategory = rulesService.viewTerritoryCategory(groupId, territoryCategoryId);
		model.addAttribute("group", territoryCategory.getGroup());
		model.addAttribute("territoryCategoryForm", new TerritoryCategoryInput(territoryCategory));
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
		model.addAttribute("territoryTypeForm", new TerritoryTypeInput(groupId, territoryCategoryId));
		return "wh40kskirmish/territorytypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorytype/save", method=RequestMethod.POST)
	public Object saveTerritoryTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("territoryTypeForm") final TerritoryTypeInput territoryTypeForm,
			final BindingResult bindingResult) {
		verifyURLGroupIdMatchesForm(groupId, territoryTypeForm);
		if(territoryTypePersister.save(territoryTypeForm, bindingResult)) {
			log.info(String.format("Saved territory type: %s", territoryTypeForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", territoryTypeForm));
			model.addAttribute("territoryCategory", rulesService.viewTerritoryCategory(groupId, territoryTypeForm.getTerritoryCategoryId()));
			return "wh40kskirmish/territorytypes_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP_RULES)
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
		final TerritoryTypeOutput territoryType = rulesService.viewTerritoryType(groupId, territoryCategoryId, territoryTypeId);
		model.addAttribute("territoryCategory", territoryType.getTerritoryCategory());
		model.addAttribute("territoryTypeForm", new TerritoryTypeInput(territoryType));
		return "wh40kskirmish/territorytypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/skillcategory/create", method=RequestMethod.GET)
	public String createSkillCategoryForm(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing create skill category form for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("skillCategoryForm", new SkillCategoryInput(groupId));
		return "wh40kskirmish/skillcategories_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/skillcategory/save", method=RequestMethod.POST)
	public Object saveSkillCategoryForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("skillCategoryForm") final SkillCategoryInput skillCategoryForm,
			final BindingResult bindingResult) {
		verifyURLGroupIdMatchesForm(groupId, skillCategoryForm);
		if(skillCategoryPersister.save(skillCategoryForm, bindingResult)) {
			log.info(String.format("Saved skill category: %s", skillCategoryForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", skillCategoryForm));
			model.addAttribute("group", groupService.viewGroup(groupId));
			return "wh40kskirmish/skillcategories_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP_RULES)
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
		final SkillCategoryOutput skillCategory = rulesService.viewSkillCategory(groupId, skillCategoryId);
		model.addAttribute("group", skillCategory.getGroup());
		model.addAttribute("skillCategoryForm", new SkillCategoryInput(skillCategory));
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
		model.addAttribute("skillForm", new SkillInput(groupId, skillCategoryId));
		return "wh40kskirmish/skills_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/skill/save", method=RequestMethod.POST)
	public Object saveSkillForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("skillForm") final SkillInput skillForm,
			final BindingResult bindingResult) {
		verifyURLGroupIdMatchesForm(groupId, skillForm);
		if(skillPersister.save(skillForm, bindingResult)) {
			log.info(String.format("Saved skill: %s", skillForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", skillForm));
			model.addAttribute("skillCategory", rulesService.viewSkillCategory(groupId, skillForm.getSkillCategoryId()));
			return "wh40kskirmish/skills_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP_RULES)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/skill/{" + ATTR_SKILL_ID + "}")
	public String viewSkill(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_SKILL_ID) final Long skillId,
			final Model model) {
		model.addAttribute("skill", rulesService.viewSkill(groupId, skillId));
		return "wh40kskirmish/skills_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/skill/{" + ATTR_SKILL_ID + "}/edit", method=RequestMethod.GET)
	public String editSkillForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_SKILL_ID) final Long skillId,
			final Model model) {
		log.info(String.format("Viewing edit skill form for skill %d in group %d", skillId, groupId));
		final SkillOutput skill = rulesService.viewSkill(groupId, skillId);
		model.addAttribute("skillCategory", skill.getSkillCategory());
		model.addAttribute("skillForm", new SkillInput(skill));
		return "wh40kskirmish/skills_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/itemcategory/create", method=RequestMethod.GET)
	public String createItemCategoryForm(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing create item category form for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("itemCategoryForm", new ItemCategoryInput(groupId));
		return "wh40kskirmish/itemcategories_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/itemcategory/save", method=RequestMethod.POST)
	public Object saveItemCategoryForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("itemCategoryForm") final ItemCategoryInput itemCategoryForm,
			final BindingResult bindingResult) {
		verifyURLGroupIdMatchesForm(groupId, itemCategoryForm);
		if(itemCategoryPersister.save(itemCategoryForm, bindingResult)) {
			log.info(String.format("Saved item category: %s", itemCategoryForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", itemCategoryForm));
			model.addAttribute("group", groupService.viewGroup(groupId));
			return "wh40kskirmish/itemcategories_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP_RULES)
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
		final ItemCategoryOutput itemCategory = rulesService.viewItemCategory(groupId, itemCategoryId);
		model.addAttribute("group", itemCategory.getGroup());
		model.addAttribute("itemCategoryForm", new ItemCategoryInput(itemCategory));
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
		model.addAttribute("itemTypeForm", new ItemTypeInput(groupId, itemCategoryId));
		return "wh40kskirmish/itemtypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/itemtype/save", method=RequestMethod.POST)
	public Object saveItemTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("itemTypeForm") final ItemTypeInput itemTypeForm,
			final BindingResult bindingResult) {
		verifyURLGroupIdMatchesForm(groupId, itemTypeForm);
		if(itemTypePersister.save(itemTypeForm, bindingResult)) {
			log.info(String.format("Saved item type: %s", itemTypeForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", itemTypeForm));
			model.addAttribute("itemCategory", rulesService.viewItemCategory(groupId, itemTypeForm.getItemCategoryId()));
			return "wh40kskirmish/itemtypes_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP_RULES)
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
		final ItemTypeOutput itemType = rulesService.viewItemType(groupId, itemCategoryId, itemTypeId);
		model.addAttribute("itemCategory", itemType.getItemCategory());
		model.addAttribute("itemTypeForm", new ItemTypeInput(itemType));
		return "wh40kskirmish/itemtypes_edit";
	}
}

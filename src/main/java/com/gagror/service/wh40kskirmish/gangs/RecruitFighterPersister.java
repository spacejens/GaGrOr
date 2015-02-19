package com.gagror.service.wh40kskirmish.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.wh40kskirmish.gangs.FighterEntity;
import com.gagror.data.wh40kskirmish.gangs.FighterRecruitInput;
import com.gagror.data.wh40kskirmish.gangs.FighterRepository;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeRepository;
import com.gagror.service.AbstractPersister;

@Service
public class RecruitFighterPersister extends AbstractPersister<FighterRecruitInput, FighterEntity, GangEntity> {

	@Autowired
	GangRepository gangRepository;

	@Autowired
	FighterTypeRepository fighterTypeRepository;

	@Autowired
	FighterRepository fighterRepository;

	@Override
	protected void validateForm(final FighterRecruitInput form, final BindingResult bindingResult) {
		// Nothing to validate that isn't already validated by annotations
	}

	@Override
	protected GangEntity loadContext(final FighterRecruitInput form) {
		return gangRepository.load(form.getGroupId(), form.getGangId());
	}

	@Override
	protected void validateFormVsContext(final FighterRecruitInput form, final BindingResult bindingResult, final GangEntity context) {
		final FighterTypeEntity fighterType = loadFighterType(form);
		// Validate that the selected fighter type is available for the gang
		if(! context.getGangType().equals(fighterType.getGangType())) {
			// TODO Validate that the selected fighter type is available for the gang
		}
		// Validate that the gang can afford to recruit the fighter
		if(context.getMoney() < fighterType.getCost()) {
			form.addErrorNotEnoughMoney(bindingResult, fighterType.getCost());
		}
		// TODO Validate that one more of the selected fighter type can be recruited by the gang
		// TODO Validate that the fighter name is unique (in the gang? in the group?)
	}

	@Override
	protected boolean isCreateNew(final FighterRecruitInput form) {
		return true;
	}

	@Override
	protected FighterEntity createNew(final FighterRecruitInput form, final GangEntity context) {
		return new FighterEntity(
				context,
				loadFighterType(form));
	}

	private FighterTypeEntity loadFighterType(final FighterRecruitInput form) {
		return fighterTypeRepository.load(form.getGroupId(), form.getFighterTypeId());
	}

	@Override
	protected void updateValues(final FighterRecruitInput form, final FighterEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected FighterEntity makePersistent(final FighterEntity entity) {
		return fighterRepository.persist(entity);
	}

	@Override
	protected void postPersistenceUpdate(final FighterEntity entity) {
		// Pay for the recruited fighter
		entity.getGang().setMoney(entity.getGang().getMoney() - entity.getFighterType().getCost());
	}
}

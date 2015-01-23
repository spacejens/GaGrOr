package com.gagror.data.wh40kskirmish.rules.gangs;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.gagror.data.AbstractIdentifiableNamedInput;
import com.gagror.data.wh40kskirmish.rules.experience.Wh40kSkirmishExperienceLevelInput;
import com.gagror.data.wh40kskirmish.rules.experience.Wh40kSkirmishExperienceLevelOutput;

@NoArgsConstructor
public class Wh40kSkirmishGangTypeInput extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishGangTypeOutput> {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	@NotEmpty
	@Valid
	private List<Wh40kSkirmishExperienceLevelInput> experienceLevels;

	public Wh40kSkirmishGangTypeInput(final Wh40kSkirmishGangTypeOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
		// Already existing experience levels
		setExperienceLevels(new ArrayList<Wh40kSkirmishExperienceLevelInput>());
		for(final Wh40kSkirmishExperienceLevelOutput experienceLevel : currentState.getExperienceLevels()) {
			getExperienceLevels().add(new Wh40kSkirmishExperienceLevelInput(experienceLevel));
		}
	}

	public Wh40kSkirmishGangTypeInput(final Long groupId) {
		setGroupId(groupId);
		// Default experience level
		final List<Wh40kSkirmishExperienceLevelInput> experienceLevelDefaults = new ArrayList<>();
		final Wh40kSkirmishExperienceLevelInput experienceLevelDefault = new Wh40kSkirmishExperienceLevelInput();
		experienceLevelDefault.setName("");
		experienceLevelDefault.setExperiencePoints(0);
		experienceLevelDefaults.add(experienceLevelDefault);
		setExperienceLevels(experienceLevelDefaults);
	}

	public void addErrorExperienceLevelsMustStartAtZero(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(
				bindingResult.getObjectName(), "experienceLevels", "one experience level must start at zero"));
	}

	public void addErrorExperienceLevelsMustBeUnique(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(
				bindingResult.getObjectName(), "experienceLevels", "experience levels must be unique"));
	}
}

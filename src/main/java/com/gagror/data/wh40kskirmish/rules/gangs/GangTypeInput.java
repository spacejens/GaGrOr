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
import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelInput;
import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelOutput;

@NoArgsConstructor
public class GangTypeInput extends AbstractIdentifiableNamedInput<Long, GangTypeOutput> {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	@NotEmpty
	@Valid
	private List<ExperienceLevelInput> experienceLevels;

	public GangTypeInput(final GangTypeOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
		// Already existing experience levels
		setExperienceLevels(new ArrayList<ExperienceLevelInput>());
		for(final ExperienceLevelOutput experienceLevel : currentState.getExperienceLevels()) {
			getExperienceLevels().add(new ExperienceLevelInput(experienceLevel));
		}
	}

	public GangTypeInput(final Long groupId) {
		setGroupId(groupId);
		// Default experience level
		final List<ExperienceLevelInput> experienceLevelDefaults = new ArrayList<>();
		final ExperienceLevelInput experienceLevelDefault = new ExperienceLevelInput();
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

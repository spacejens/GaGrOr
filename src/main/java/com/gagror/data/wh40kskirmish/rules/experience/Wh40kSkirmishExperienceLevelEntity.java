package com.gagror.data.wh40kskirmish.rules.experience;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractEditableNamedEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_experiencelevel")
public class Wh40kSkirmishExperienceLevelEntity
extends AbstractEditableNamedEntity
implements Wh40kSkirmishExperiencePoints {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private Wh40kSkirmishGangTypeEntity gangType;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int experiencePoints;

	public Wh40kSkirmishExperienceLevelEntity(final Wh40kSkirmishGangTypeEntity gangType) {
		this.gangType = gangType;
		// Add the new entity to the referencing collection
		gangType.getExperienceLevels().add(this);
	}
}

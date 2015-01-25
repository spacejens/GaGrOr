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
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_experiencelevel")
public class ExperienceLevelEntity
extends AbstractEditableNamedEntity
implements ExperiencePoints {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private GangTypeEntity gangType;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int experiencePoints;

	public ExperienceLevelEntity(final GangTypeEntity gangType) {
		this.gangType = gangType;
		// Add the new entity to the referencing collection
		gangType.getExperienceLevels().add(this);
	}
}

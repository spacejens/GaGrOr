package com.gagror.data.wh40kskirmish;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.gagror.data.AbstractEditableNamedEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_gangtype")
public class Wh40kSkirmishGangTypeEntity extends AbstractEditableNamedEntity {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private Wh40kSkirmishRulesEntity rules;

	@OneToMany(mappedBy="gangType", fetch=FetchType.LAZY)
	@Getter
	private Set<Wh40kSkirmishFactionEntity> factions;

	@OneToMany(mappedBy="gangType", fetch=FetchType.LAZY)
	@Getter
	private Set<Wh40kSkirmishRaceEntity> races;

	public Wh40kSkirmishGangTypeEntity(final Wh40kSkirmishRulesEntity rules) {
		this.rules = rules;
		// Add the new entity to the referencing collection
		rules.getGangTypes().add(this);
	}
}

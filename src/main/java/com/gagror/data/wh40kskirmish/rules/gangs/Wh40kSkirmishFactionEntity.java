package com.gagror.data.wh40kskirmish.rules.gangs;

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
import com.gagror.data.wh40kskirmish.gangs.Wh40kSkirmishGangEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_faction")
public class Wh40kSkirmishFactionEntity extends AbstractEditableNamedEntity {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private Wh40kSkirmishGangTypeEntity gangType;

	@OneToMany(mappedBy="faction", fetch=FetchType.LAZY)
	@Getter
	private Set<Wh40kSkirmishGangEntity> gangs;

	public Wh40kSkirmishFactionEntity(final Wh40kSkirmishGangTypeEntity gangType) {
		this.gangType = gangType;
		// Add the new entity to the referencing collection
		gangType.getFactions().add(this);
	}
}

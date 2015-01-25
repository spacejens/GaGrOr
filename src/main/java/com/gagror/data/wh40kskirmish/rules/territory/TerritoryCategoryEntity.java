package com.gagror.data.wh40kskirmish.rules.territory;

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
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_territorycategory")
public class TerritoryCategoryEntity extends AbstractEditableNamedEntity {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private Wh40kSkirmishRulesEntity rules;

	@OneToMany(mappedBy="territoryCategory", fetch=FetchType.LAZY)
	@Getter
	private Set<TerritoryTypeEntity> territoryTypes;

	public TerritoryCategoryEntity(final Wh40kSkirmishRulesEntity rules) {
		this.rules = rules;
		// Add the new entity to the referencing collection
		rules.getTerritoryCategories().add(this);
	}
}

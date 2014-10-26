package com.gagror.data.group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.gagror.data.Identifiable;
import com.gagror.data.account.AccountEntity;

@Data
@NoArgsConstructor
@Entity
@Table(name="groupmember")
@EqualsAndHashCode(of="id")
public class GroupMemberEntity implements Identifiable<Long> {

	@Id
	@GeneratedValue
	private Long id;

	@Version
	private Long version;

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	private GroupEntity group;

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	private AccountEntity account;

	@Column(nullable = false)
	private MemberType memberType;
}

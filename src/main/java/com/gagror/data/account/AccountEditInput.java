package com.gagror.data.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@NoArgsConstructor
public class AccountEditInput extends RegisterInput {

	private Long id;

	private Long version;

	public AccountEditInput(final AccountEditOutput currentState) {
		setId(currentState.getId());
		setVersion(currentState.getVersion());
		setUsername(currentState.getUsername());
	}
}

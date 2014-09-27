package com.gagror.data.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true, exclude="passwordRepeat")
public class RegisterInput extends LoginCredentialsInput {

	private String passwordRepeat;
}

package com.gagror.data.account;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(exclude={"password","encryptedPassword"})
public class LoginCredentialsInput {

	private String username;

	private String password;

	private String encryptedPassword;

	private String salt;

	public LoginCredentialsInput(final RegisterInput registerForm) {
		setUsername(registerForm.getUsername());
		setEncryptedPassword(registerForm.getEncryptedPassword());
		setSalt(registerForm.getSalt());
	}
}

package com.gagror.data.account;

import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Data
@NoArgsConstructor
@ToString(exclude={"password","encryptedPassword"})
public class LoginCredentialsInput {

	@Size(min=3, max=64)
	private String username;

	private String password;

	private String encryptedPassword;

	private String salt;

	public LoginCredentialsInput(final RegisterInput registerForm) {
		setUsername(registerForm.getUsername());
		setEncryptedPassword(registerForm.getEncryptedPassword());
		setSalt(registerForm.getSalt());
	}

	public void addErrorLoginFailed(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "password", "incorrect"));
	}
}

package com.gagror.service.accesscontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.AccountType;

@RunWith(MockitoJUnitRunner.class)
public class PermissionEditAccountUnitTest {

	private static final Long ID_ACCOUNT = 3L;
	private static final Long ID_OTHER = 11L;

	PermissionEditAccount instance;

	@Mock
	AccountRepository accountRepository;

	@Mock
	AccountEntity account;

	@Mock
	AccountEntity editedAccount;

	@Test
	public void hasPermission_ownAccount_ok() {
		assertTrue("User should be allowed to edit own account", instance.hasPermission(ID_ACCOUNT, account));
	}

	@Test
	public void hasPermission_anotherAccount_notOk() {
		assertFalse("Standard user should not be allowed to edit another account", instance.hasPermission(ID_OTHER, account));
	}

	@Test
	public void hasPermission_anotherAccount_systemOwner_ok() {
		when(account.getAccountType()).thenReturn(AccountType.SYSTEM_OWNER);
		assertTrue("System owner should be allowed to edit another account", instance.hasPermission(ID_OTHER, account));
	}

	@Test
	public void parseId() {
		assertEquals("Unexpected parse result", ID_OTHER, instance.parseId(ID_OTHER.toString()));
	}

	@Before
	public void setupEditedAccount() {
		when(editedAccount.getAccountType()).thenReturn(AccountType.STANDARD);
		when(editedAccount.getId()).thenReturn(ID_OTHER);
	}

	@Before
	public void setupAccount() {
		when(account.getAccountType()).thenReturn(AccountType.STANDARD);
		when(account.getId()).thenReturn(ID_ACCOUNT);
	}

	@Before
	public void setupAccountRepository() {
		when(accountRepository.load(ID_OTHER)).thenReturn(editedAccount);
	}

	@Before
	public void setupInstance() {
		instance = new PermissionEditAccount();
		instance.accountRepository = accountRepository;
	}
}

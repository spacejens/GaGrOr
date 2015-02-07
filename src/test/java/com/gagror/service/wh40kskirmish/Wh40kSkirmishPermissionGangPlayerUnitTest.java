package com.gagror.service.wh40kskirmish;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishPermissionGangPlayerUnitTest {

	private static final Long GANG_ID = 12L;
	private static final Long ACCOUNT_ID_PLAYER = 34L;
	private static final Long ACCOUNT_ID_OTHER = 56L;

	Wh40kSkirmishPermissionGangPlayer instance;

	@Mock
	GangRepository gangRepository;

	@Mock
	GangEntity gang;

	@Mock
	AccountEntity player;

	@Mock
	AccountEntity anotherAccount;

	@Test
	public void hasPermission_player_ok() {
		assertTrue("Player should have permission", instance.hasPermission(GANG_ID, player));
	}

	@Test
	public void hasPermission_anotherAccount_notOk() {
		assertFalse("Other accounts should not have permission", instance.hasPermission(GANG_ID, anotherAccount));
	}

	@Test(expected=DataNotFoundException.class)
	public void hasPermission_gangNotFound() {
		doThrow(DataNotFoundException.class).when(gangRepository).load(GANG_ID);
		instance.hasPermission(GANG_ID, player);
	}

	@Before
	public void setupAccounts() {
		when(player.getId()).thenReturn(ACCOUNT_ID_PLAYER);
		when(anotherAccount.getId()).thenReturn(ACCOUNT_ID_OTHER);
	}

	@Before
	public void setupGang() {
		when(gang.getId()).thenReturn(GANG_ID);
		when(gang.getPlayer()).thenReturn(player);
		when(gangRepository.load(GANG_ID)).thenReturn(gang);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishPermissionGangPlayer();
		instance.gangRepository = gangRepository;
	}
}

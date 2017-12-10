package util;

import static org.junit.Assert.*;

import org.junit.Test;

import data.AuthoritySettings;

public class TestHash {

	@Test
	public void testHashAndCredentialHelper() {
		System.out.println(CredentialHelper.GetHash("test"));
		System.out.println(CredentialHelper.securePassword(CredentialHelper.GetHash("test")));
		assertTrue(CredentialHelper.GetHash("haha").equals(CredentialHelper.GetHash("haha")));
		assertTrue(!CredentialHelper.GetHash("1337").equals(CredentialHelper.GetHash("1336")));
		
		AuthoritySettings settings = SettingsHelper.getSettings(AuthoritySettings.class);
		String salt = settings.getSalt();
		
		//check settings
		assertTrue(settings != null);
		assertTrue(salt != null && !salt.equals(""));
		
		//check salting
		String hash = CredentialHelper.GetHash("mobility");
		assertTrue(!hash.equals(CredentialHelper.securePassword(hash)));
		
	}
}

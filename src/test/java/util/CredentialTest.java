package util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import data.User;

public class CredentialTest {
	private String pwd = "demo";
	private String pwdHash;

	@Before
	public void Initialize() {
		pwdHash = Credential.GetHash(pwd);
	}

	@Test
	public void ValidTest() {
		assertEquals(true, Credential.IsValid("blubr", pwdHash));
		assertEquals(false, Credential.IsValid("blubr", "no valid password"));
		assertEquals(false, Credential.IsValid("no valid mail", pwdHash));
	}
}

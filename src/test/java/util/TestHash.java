package util;

import org.junit.Test;

public class TestHash {

	@Test
	public void testHash() {
		System.out.println(CredentialHelper.GetHash("test"));
		

		System.out.println(CredentialHelper.securePassword(CredentialHelper.GetHash("test")));
	}
}

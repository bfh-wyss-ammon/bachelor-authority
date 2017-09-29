package util;

import org.junit.Test;

public class TestHash {

	@Test
	public void testHash() {
		System.out.println(Credential.GetHash("test"));
		

		System.out.println(Credential.securePassword(Credential.GetHash("test")));
	}
}

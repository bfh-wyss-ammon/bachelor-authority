package util;

import static org.junit.Assert.assertNotNull;


import org.junit.Before;
import org.junit.Test;

import data.GroupSignature;
import data.PublicKey;

public class SignatureTest {
	GroupSignature groupSig = null;

	@Before
	public void Intilaize() {
		
	}
	
	@Test
	public void Save() {
		while (groupSig == null) {
			groupSig = Signature.generateGroupSignature();
		}
		assertNotNull(Database.Save(PublicKey.class, groupSig));
	}
	
	@Test
	public void Load() {
		GroupSignature gs = Database.Get(GroupSignature.class, 1);
		assertNotNull(gs);
		assertNotNull(gs.getManagerKey());
		assertNotNull(gs.getPublicKey());
	}
}  

package util;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import data.GroupKey;
import data.SecretKey;
import data.Signature;
import data.Membership;
import data.GroupJoinRequest;
import data.GroupJoinResponse;
import data.PublicKey;
import data.User;

public class SignatureTest {
	GroupKey groupKey = null;

	@Before
	public void Intilaize() {
		
	}
	
	@Test
	public void Save() {
		while (groupKey == null) {
			groupKey = SignatureHelper.generateGroupKey();
		}
		assertNotNull(Database.Save(PublicKey.class, groupKey));
	}
	
	@Test
	public void Load() {
		GroupKey gs = Database.Get(GroupKey.class, 2);
		assertNotNull(gs);
		assertNotNull(gs.getManagerKey());
		assertNotNull(gs.getPublicKey());
		assertNotNull(gs.getPublicKey().getN());
	}
	
	@Test
	public void Join() {
		//User u = Database.Get(User.class, 1);
		
		// todo: gruppe mit am wenigsten mitglieder / select 1
		
		GroupKey gk = SignatureHelper.generateGroupKey();
		assertNotNull(gk);
		assertNotNull(gk.getManagerKey());
		assertNotNull(gk.getPublicKey());
		
		// auf App
		SecretKey mk = SignatureHelper.initMemberKey(gk.getPublicKey());
		GroupJoinRequest gjr = SignatureHelper.groupJoinRequest(mk);
		
		// auf Server / Authority
		GroupJoinResponse gjresponse = SignatureHelper.groupJoinResponse(gk.getManagerKey(), gk.getPublicKey(), gjr);

		// auf App
		SignatureHelper.updateMemberKey(gjresponse, mk);
				
		// auf server:  big Y => Membership
		Membership ms = new Membership();
		
		// discuss: von wo
		//ms.setUser(u);
		ms.setGroupKey(gk);
		ms.setBigY(gjr.getBigY());
		//Database.Save(Membership.class, ms);
		
		byte[] blubr = "hello world".getBytes();
		byte[] blubr2 = "hello world2".getBytes();
		
		byte[] testmessage = new BigInteger("1990").toByteArray();
		
		Signature sig = SignatureHelper.sign(testmessage, mk, gk.getPublicKey());
		
		
		assertEquals(true, SignatureHelper.verify(gk.getPublicKey(), testmessage, sig));
		//assertEquals(false, SignatureHelper.verify(gk.getPublicKey(), blubr2, sig));
		
		
		
		
	}
}  

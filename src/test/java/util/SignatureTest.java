package util;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import data.DbGroup;
import data.DbManagerKey;
import data.DbPublicKey;
import demo.DemoSecretKey;
import keys.SecretKey;
import requests.JoinRequest;
import responses.JoinResponse;
import signatures.Signature;


public class SignatureTest {
	
	@Test
	public void SignTestWithStoredData() {
		DbGroup group = Database.Get(DbGroup.class, 1);
		
		SecretKey memberKey = new DemoSecretKey();
		JoinHelper.init(group.getPublicKey(), memberKey);

		JoinRequest joinRequest = new JoinRequest(memberKey);

		JoinResponse joinResponse = JoinHelper.join(group.getPublicKey(), group.getManagerKey(), joinRequest);

		memberKey.maintainResponse(joinResponse);

		byte[] testmessage = new BigInteger("1990").toByteArray();

		Signature signature = SignHelper.sign(memberKey, group.getPublicKey(), testmessage);

		assertTrue(VerifyHelper.verify(group.getPublicKey(), signature, testmessage));
	}
	
	@Test
	public void testGenerateAndSave() {
		DbPublicKey publicKey = new DbPublicKey();
		DbManagerKey managerKey = new DbManagerKey();
		
		Generator.generate(publicKey, managerKey);
		
		DbGroup group = new DbGroup();
		group.setPublicKey(publicKey);
		group.setManagerKey(managerKey);
		
		Database.Save(DbGroup.class, group);
				
	
	
	}
	
}  

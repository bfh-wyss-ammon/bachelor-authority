package util;


import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import data.DbGroup;
import data.DbManagerKey;
import data.DbMembership;
import data.DbPublicKey;
import data.DbUser;
import demo.DemoSecretKey;
import keys.SecretKey;
import requests.JoinRequest;
import responses.JoinResponse;
import signatures.Signature;

public class Tests {
	private String demoUserMail = "demo@user.ch";
	private String demoUserPassword = "test";
	private String demoUserPasswordHash;
	private String demoUserPasswordSalt;
	
	@Before
	public void init() {
		demoUserPasswordHash = Credential.GetHash(demoUserPassword);
		demoUserPasswordSalt = Credential.securePassword(demoUserPasswordHash);
	}
	
	
	
	@Test
	public void test() {		
		DbUser user = new DbUser();
		DbPublicKey publicKey = new DbPublicKey();
		DbManagerKey managerKey = new DbManagerKey();
		DbGroup group = new DbGroup();
		DbMembership membership = new DbMembership();
		membership.setApproved(false);
		try {
			user.setMail(demoUserMail);
			user.setPassword(demoUserPasswordSalt);
			Database.Save(DbUser.class, user);
			assertNotNull(user.getUserId());
						
			Generator.generate(SettingsHelper.getSettings(), publicKey, managerKey);
			
			group.setManagerKey(managerKey);
			group.setPublicKey(publicKey);
			Database.Save(DbGroup.class, group);
			assertNotNull(group.getGroupId());
			
			membership.setGroup(group);
			membership.setUser(user);
			
			Database.Save(DbMembership.class, membership);
			
			// SmartPhone A
			SecretKey memberKeyA = new DemoSecretKey();
			JoinHelper.init(SettingsHelper.getSettings(), Database.Get(DbPublicKey.class, publicKey.getPublicKeyId()), memberKeyA);

			JoinRequest joinRequestA = new JoinRequest(memberKeyA);
			

			JoinResponse joinResponseA = JoinHelper.join(SettingsHelper.getSettings(), publicKey, managerKey, joinRequestA);
			membership.setBigY(joinRequestA.bigY());
			Database.Update(membership);
			
			memberKeyA.maintainResponse(joinResponseA);
			membership.setApproved(true);
			Database.Update(membership);
			
			byte[] testmessage = new BigInteger("1990").toByteArray();

			Signature signatureA = SignHelper.sign(SettingsHelper.getSettings(), memberKeyA, publicKey, testmessage);

			assertTrue(VerifyHelper.verify(SettingsHelper.getSettings(), publicKey, signatureA, testmessage));

			
			
		}
		finally {
			Database.Delete(membership);
			Database.Delete(user);
			Database.Delete(group);
		}
		
	}

}

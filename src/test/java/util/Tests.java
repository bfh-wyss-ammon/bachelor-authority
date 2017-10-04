package util;


import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Date;

import org.eclipse.jetty.server.Authentication.User;
import org.junit.Before;
import org.junit.Test;

import data.DbGroup;
import data.DbManagerKey;
import data.DbPublicKey;
import data.DbMembership;
import data.DbUser;
import demo.DemoSecretKey;
import keys.SecretKey;
import requests.JoinRequest;
import responses.JoinResponse;
import signatures.Signature;

public class Tests {
	private String demoUserMail = "test@user.ch";
	private String demoUserPassword = "test";
	private String demoUserPasswordHash;
	private String demoUserPasswordSalt;
	
	@Before
	public void init() {
		demoUserPasswordHash = CredentialHelper.GetHash(demoUserPassword);
		demoUserPasswordSalt = CredentialHelper.securePassword(demoUserPasswordHash);
	}
	
	@Test
	public void userLoading() {
		assertNotNull(DatabaseHelper.Get(DbUser.class));
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
			DatabaseHelper.Save(DbUser.class, user);
			assertNotNull(user.getUserId());
						
			Generator.generate(SettingsHelper.getSettings(), publicKey, managerKey);
			
			group.setManagerKey(managerKey);
			group.setPublicKey(publicKey);
			DatabaseHelper.Save(DbGroup.class, group);
			assertNotNull(group.getGroupId());
			
			membership.setGroup(group);
			membership.setUser(user);
			
			DatabaseHelper.Save(DbMembership.class, membership);
			
			// SmartPhone A
			SecretKey memberKeyA = new DemoSecretKey();
			JoinHelper.init(SettingsHelper.getSettings(), DatabaseHelper.Get(DbPublicKey.class, publicKey.getPublicKeyId()), memberKeyA);

			JoinRequest joinRequestA = new JoinRequest(memberKeyA);
			

			JoinResponse joinResponseA = JoinHelper.join(SettingsHelper.getSettings(), publicKey, managerKey, joinRequestA);
			membership.setBigY(joinRequestA.bigY());
			DatabaseHelper.Update(membership);
			
			memberKeyA.maintainResponse(joinResponseA);
			membership.setApproved(true);
			membership.setCreated(new Date());
			DatabaseHelper.Update(membership);
			
			byte[] testmessage = new BigInteger("1990").toByteArray();

			Signature signatureA = SignHelper.sign(SettingsHelper.getSettings(), memberKeyA, publicKey, testmessage);

			assertTrue(VerifyHelper.verify(SettingsHelper.getSettings(), publicKey, signatureA, testmessage));

			
			
		}
		finally {
			DatabaseHelper.Delete(membership);
			DatabaseHelper.Delete(user);
			DatabaseHelper.Delete(group);
		}
		
	}

}

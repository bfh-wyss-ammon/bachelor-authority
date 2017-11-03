package util;


import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import data.DbGroup;
import data.DbManagerKey;
import data.DbPublicKey;
import data.DbMembership;
import data.DbUser;
import data.AuthoritySettings;
import data.BaseSignature;
import demo.DemoSecretKey;
import keys.SecretKey;
import requests.JoinRequest;
import responses.JoinResponse;

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
	public void loadMembership() {
		DbMembership ship =DatabaseHelper.Get(DbMembership.class, "userId = 9");
		
		assertNotNull(ship);
		
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
			user.setId(demoUserMail);
			user.setPassword(demoUserPasswordSalt);
			DatabaseHelper.Save(DbUser.class, user);
			assertNotNull(user.getUserId());
						
			Generator.generate(SettingsHelper.getSettings(AuthoritySettings.class), publicKey, managerKey);
			
			group.setManagerKey(managerKey);
			group.setPublicKey(publicKey);
			DatabaseHelper.Save(DbGroup.class, group);
			assertNotNull(group.getGroupId());
			
			membership.setGroup(group);
			membership.setUser(user);
			
			DatabaseHelper.Save(DbMembership.class, membership);
			
			// SmartPhone A
			SecretKey memberKeyA = new DemoSecretKey();
			JoinHelper.init(SettingsHelper.getSettings(AuthoritySettings.class), DatabaseHelper.Get(DbPublicKey.class, publicKey.getPublicKeyId()), memberKeyA);

			JoinRequest joinRequestA = new JoinRequest(memberKeyA);
			

			JoinResponse joinResponseA = JoinHelper.join(SettingsHelper.getSettings(AuthoritySettings.class), publicKey, managerKey, joinRequestA);
			membership.setBigY(joinRequestA.bigY());
			DatabaseHelper.Update(membership);
			
			memberKeyA.maintainResponse(joinResponseA);
			membership.setApproved(true);
			membership.setCreated(new Date());
			DatabaseHelper.Update(membership);
			
			byte[] testmessage = new BigInteger("1990").toByteArray();

			BaseSignature signatureA = new BaseSignature();
			
			SignHelper.sign(SettingsHelper.getSettings(AuthoritySettings.class), memberKeyA, publicKey, testmessage, signatureA);

			assertTrue(VerifyHelper.verify(SettingsHelper.getSettings(AuthoritySettings.class), publicKey, signatureA, testmessage));

			
			
		}
		finally {
			DatabaseHelper.Delete(membership);
			DatabaseHelper.Delete(user);
			DatabaseHelper.Delete(group);
		}
		
	}

}

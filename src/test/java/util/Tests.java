/**
 *   Copyright 2018 Pascal Ammon, Gabriel Wyss
 * 
 * 	 Implementation eines anonymen Mobility Pricing Systems auf Basis eines Gruppensignaturschemas
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

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
import settings.DefaultSettings;

public class Tests {
	private String demoUserMail = "test@user.ch";
	private String demoUserPassword = "test";
	private String demoUserPasswordHash;
	private String demoUserPasswordSalt;
	DbUser user = new DbUser();

	@Before
	public void init() {
		demoUserPasswordHash = CredentialHelper.GetHash(demoUserPassword);
		demoUserPasswordSalt = CredentialHelper.securePassword(demoUserPasswordHash);
		user.setPassword(demoUserPasswordSalt);
		user.setFirstname("Best Test");
		user.setLastname("Ever");
		user.setId(demoUserMail);
		DatabaseHelper.Save(DbUser.class, user);
	}

	@Test
	public void userLoading() {
		DbUser user = DatabaseHelper.Get(DbUser.class, "id ='" + demoUserMail + "'");

		// test database user handling
		assertNotNull(user);
		assertNotNull(user.getFirstname());
		assertNotNull(user.getId());
		assertNotNull(user.getLastname());
		assertNotNull(user.getPassword());
		assertNotNull(user.getUserId());
	}

	@Test
	public void completeUserAndMembershipTest() {
		DbPublicKey publicKey = new DbPublicKey();
		DbManagerKey managerKey = new DbManagerKey();
		DbGroup group = new DbGroup();
		DbMembership membership = new DbMembership();
		membership.setApproved(false);
		try {
			assertNotNull(user.getUserId());
			Generator.generate(SettingsHelper.getSettings(DefaultSettings.class), publicKey, managerKey);
			group.setManagerKey(managerKey);
			group.setPublicKey(publicKey);
			DatabaseHelper.Save(DbGroup.class, group);
			DbGroup loadedGroup = DatabaseHelper.Get(DbGroup.class, "groupId=" + group.getGroupId());

			// test database group handling
			assertNotNull(group.getGroupId());
			assertNotNull(group);
			assertNotNull(loadedGroup);
			assertNotNull(managerKey);
			assertNotNull(publicKey);
			assertNotNull(loadedGroup.getManagerKey());
			assertNotNull(loadedGroup.getPublicKey());
			assertNotNull(loadedGroup.getGroupId());

			membership.setGroup(group);
			membership.setUser(user);
			DatabaseHelper.Save(DbMembership.class, membership);
			DbMembership loadedShip = DatabaseHelper.Get(DbMembership.class, "userId=" + user.getUserId());

			// test database membership handling
			assertNotNull(membership);
			assertNotNull(membership.getGroup());
			assertNotNull(membership.getApproved());
			assertNotNull(membership.getUser());
			assertNotNull(loadedShip);
			assertNotNull(loadedShip.getGroup());
			assertNotNull(loadedShip.getApproved());
			assertNotNull(loadedShip.getUser());

			// SmartPhone A
			SecretKey memberKeyA = new DemoSecretKey();
			JoinHelper.init(SettingsHelper.getSettings(DefaultSettings.class),
					DatabaseHelper.Get(DbPublicKey.class, publicKey.getPublicKeyId()), memberKeyA);
			JoinRequest joinRequestA = new JoinRequest(memberKeyA);
			JoinResponse joinResponseA = JoinHelper.join(SettingsHelper.getSettings(DefaultSettings.class), publicKey,
					managerKey, joinRequestA);
			membership.setBigY(joinRequestA.bigY());
			DatabaseHelper.Update(membership);
			memberKeyA.maintainResponse(joinResponseA);
			membership.setApproved(true);
			membership.setCreated(new Date());
			DatabaseHelper.Update(membership);

			// test correct membership updating

			DbMembership loadedShip2 = DatabaseHelper.Get(DbMembership.class, "userId=" + user.getUserId());

			// test database membership handling
			assertNotNull(membership);
			assertNotNull(membership.getGroup());
			assertNotNull(membership.getApproved());
			assertNotNull(membership.getCreated());
			assertNotNull(membership.getUser());
			assertNotNull(membership.getBigY());
			assertNotNull(loadedShip2);
			assertNotNull(loadedShip2.getGroup());
			assertNotNull(loadedShip2.getApproved());
			assertNotNull(loadedShip2.getCreated());
			assertNotNull(loadedShip2.getUser());
			assertNotNull(loadedShip2.getBigY());
			assertNotNull(loadedShip2.getMembershipId());

			DbMembership ship = DatabaseHelper.Get(DbMembership.class, "userId =" + user.getUserId());
			assertNotNull(ship);

			byte[] testmessage = new BigInteger("1990").toByteArray();
			BaseSignature signatureA = new BaseSignature();
			SignHelper.sign(SettingsHelper.getSettings(DefaultSettings.class), memberKeyA, publicKey, testmessage,
					signatureA);
			assertTrue(VerifyHelper.verify(SettingsHelper.getSettings(DefaultSettings.class), publicKey, signatureA,
					testmessage));
		} finally {
			DatabaseHelper.Delete(membership);
			DatabaseHelper.Delete(user);
			DatabaseHelper.Delete(group);
		}

	}

}

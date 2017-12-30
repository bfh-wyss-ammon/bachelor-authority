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

import org.junit.Before;
import org.junit.Test;

import data.DbGroup;
import data.DbSession;
import data.DbMembership;
import data.DbUser;

public class SessionTest {
	
	private String demoUserMail = "demo@user.ch";
	private String demoUserPassword = "test";
	private String demoUserPasswordHash;
	private String demoUserPasswordSalt;
	
	@Before
	public void init() {
		demoUserPasswordHash = CredentialHelper.GetHash(demoUserPassword);
		demoUserPasswordSalt = CredentialHelper.securePassword(demoUserPasswordHash);
	}
	
	@Test
	public void selectGroup() {
		DbGroup group = GroupHelper.getGroup();
		assertNotNull(group);
	}
	
	@Test
	public void createSession() {		
		DbUser user = DatabaseHelper.Get(DbUser.class, 42);
		DbSession session = null;
		try {			
			session = SessionHelper.getSession(user);
			DbUser u3 = session.getUser();
			DatabaseHelper.Save(DbSession.class, session);
			
			DbUser user2 = SessionHelper.getUser(session.getToken());
			
			assertNotNull(user2);
			
			DbMembership membership = MembershipHelper.getMembership(user2);
			
			assertNotNull(membership);
			assertNotNull(membership.getGroup());
			
		}
		finally {
			//Database.Delete(user);
			//Database.Delete(session);
		}
	}
}

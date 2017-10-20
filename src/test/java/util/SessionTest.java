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

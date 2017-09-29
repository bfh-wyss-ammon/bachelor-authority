package util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import data.DbJoinSession;
import data.DbMembership;
import data.DbUser;

public class SessionTest {
	
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
	public void createSession() {		
		DbUser user = Database.Get(DbUser.class, 42);
		DbJoinSession session = null;
		try {			
			session = SessionHelper.getSession(user);
			DbUser u3 = session.getUser();
			Database.Save(DbJoinSession.class, session);
			
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

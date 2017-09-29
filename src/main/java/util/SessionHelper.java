package util;

import java.util.Date;

import org.eclipse.jetty.server.session.JDBCSessionManager.Session;

import data.DbGroup;
import data.DbJoinSession;
import data.DbUser;

public class SessionHelper {

	public static DbJoinSession getSession(DbUser user) {

		String where = "userId=" + user.getUserId();
		
		if (Database.Exists(DbJoinSession.class, where)) {
			return Database.Get(DbJoinSession.class, where);			
		} else {

			DbJoinSession session = new DbJoinSession();
			session.setUser(user);
			session.setToken(java.util.UUID.randomUUID().toString());
			session.setCreated(new Date());
			return session;
		}
	}
		
	public static DbUser getUser(String token) {
		String where = "token='" + token+ "'";
		if(!Database.Exists(DbJoinSession.class, where)) {
			// todo log?? 
			return null;
		}
		DbJoinSession session = Database.Get(DbJoinSession.class, where);
		return session.getUser();
	}

}

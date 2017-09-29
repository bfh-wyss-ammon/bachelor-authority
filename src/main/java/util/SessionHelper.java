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
			DbJoinSession session = Database.Get(DbJoinSession.class, where);
			session.setCreated(new Date());
			return session;
		} else {

			DbJoinSession session = new DbJoinSession();
			session.setUser(user);
			session.setToken(java.util.UUID.randomUUID().toString());
			session.setCreated(new Date());
			return session;
		}
	}
	
	public static DbJoinSession getSession(String token) {
		String where = "token='" + token+ "'";
		
		if (token != null && Database.Exists(DbJoinSession.class, where)) {
			DbJoinSession session = Database.Get(DbJoinSession.class, where);
			session.setCreated(new Date());
			return session;
		}
		return null;
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

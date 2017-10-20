package util;

import java.util.ArrayList;
import java.util.Date;

import data.AuthoritySettings;
import data.DbJoinSession;
import data.DbUser;

public class SessionHelper {

	public static DbJoinSession getSession(DbUser user) {

		String where = "userId=" + user.getUserId();

		if (DatabaseHelper.Exists(DbJoinSession.class, where)) {
			DbJoinSession session = DatabaseHelper.Get(DbJoinSession.class, where);
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
		cleanSessions(SettingsHelper.getSettings(AuthoritySettings.class).getJoinSessionTimeout());
		String where = "token='" + token + "'";

		if (token != null && DatabaseHelper.Exists(DbJoinSession.class, where)) {
			DbJoinSession session = DatabaseHelper.Get(DbJoinSession.class, where);
			session.setCreated(new Date());
			return session;
		}
		return null;
	}

	private static void cleanSessions(int SessionTimeout) {
		Date now = new Date();
		Date limit = new Date(now.getTime() - SessionTimeout * 1000);
		DatabaseHelper.Delete(DbJoinSession.class, "created",limit);
		return;
	}

	public static DbUser getUser(String token) {
		String where = "token='" + token + "'";
		if (!DatabaseHelper.Exists(DbJoinSession.class, where)) {
			// todo log??
			return null;
		}
		DbJoinSession session = DatabaseHelper.Get(DbJoinSession.class, where);
		return session.getUser();
	}

}

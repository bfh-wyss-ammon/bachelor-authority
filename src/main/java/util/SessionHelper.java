/**
 * This helper class has static methods that handle the generation, loading and timeout of sessions of the set-up protocol.
 */

package util;

import java.util.ArrayList;
import java.util.Date;

import data.AuthoritySettings;
import data.DbSession;
import data.DbUser;

public class SessionHelper {

	public static DbSession getSession(DbUser user) {

		String where = "userId=" + user.getUserId();

		if (DatabaseHelper.Exists(DbSession.class, where)) {
			DbSession session = DatabaseHelper.Get(DbSession.class, where);
			session.setCreated(new Date());
			return session;
		} else {

			DbSession session = new DbSession();
			session.setUser(user);
			session.setToken(java.util.UUID.randomUUID().toString());
			session.setCreated(new Date());
			return session;
		}
	}

	public static DbSession getSession(String token) {
		cleanSessions(SettingsHelper.getSettings(AuthoritySettings.class).getJoinSessionTimeout());
		String where = "token='" + token + "'";

		if (token != null && DatabaseHelper.Exists(DbSession.class, where)) {
			DbSession session = DatabaseHelper.Get(DbSession.class, where);
			session.setCreated(new Date());
			return session;
		}
		return null;
	}

	private static void cleanSessions(int SessionTimeout) {
		Date now = new Date();
		Date limit = new Date(now.getTime() - SessionTimeout * 1000);
		DatabaseHelper.Delete(DbSession.class, "created", limit);
		return;
	}

	public static DbUser getUser(String token) {
		String where = "token='" + token + "'";
		if (!DatabaseHelper.Exists(DbSession.class, where)) {
			// todo log??
			return null;
		}
		DbSession session = DatabaseHelper.Get(DbSession.class, where);
		return session.getUser();
	}

}

package util;

import data.DbMembership;
import data.DbUser;

public class MembershipHelper {

	public static DbMembership getMembership(DbUser user) {
		String where = "userId=" + user.getUserId();
		if (DatabaseHelper.Exists(DbMembership.class, where)) {
			return DatabaseHelper.Get(DbMembership.class, where);
		} else {
			return null;
		}
	}
}

package util;

import data.DbSession;
import data.DbMembership;
import data.DbUser;

public class MembershipHelper {

	public static DbMembership getMembership(DbUser user) {
		String where = "userId=" + user.getUserId();
		return DatabaseHelper.Get(DbMembership.class, where);

	}
}

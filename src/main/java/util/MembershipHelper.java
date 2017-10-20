package util;

import data.DbJoinSession;
import data.DbMembership;
import data.DbUser;

public class MembershipHelper {

	public static DbMembership getMembership(DbUser user) {
		String where = "userId=" + user.getUserId();
		if (DatabaseHelper.Exists(DbMembership.class, where)) {
			DbMembership membership = DatabaseHelper.Get(DbMembership.class, where);			
			return membership;
		} else {
			return null;
		}
	}
}

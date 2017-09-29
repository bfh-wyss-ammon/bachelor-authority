package util;

import java.util.Date;

import data.DbGroup;
import data.DbJoinSession;
import data.DbMembership;
import data.DbUser;

public class MembershipHelper {

	public static DbMembership getMembership(DbUser user) {
		String where = "userId=" + user.getUserId();		
		if (Database.Exists(DbMembership.class, where)) {
			return Database.Get(DbMembership.class, where);			
		} else {
			return null;
		}
	}
}

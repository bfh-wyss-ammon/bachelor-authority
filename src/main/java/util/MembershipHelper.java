/**
 * This helper class gets a membership from the authority database for a specified user.
 */

package util;

import data.DbSession;

import java.util.Date;

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

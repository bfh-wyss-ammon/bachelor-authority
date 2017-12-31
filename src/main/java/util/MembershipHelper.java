/**
 *   Copyright 2018 Pascal Ammon, Gabriel Wyss
 * 
 * 	 Implementation eines anonymen Mobility Pricing Systems auf Basis eines Gruppensignaturschemas
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
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

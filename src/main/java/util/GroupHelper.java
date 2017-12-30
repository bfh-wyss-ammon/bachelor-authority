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
 * This helper class selects a random group from the database for the set-up protocol.
 */

package util;

import java.util.List;
import java.util.Random;
import data.DbGroup;

public class GroupHelper {

	public static DbGroup getGroup() {
		List<Integer> groupIdList = DatabaseHelper.Get(Integer.class, "groupId", DbGroup.class);
		Random random = new Random();

		return DatabaseHelper.Get(DbGroup.class, groupIdList.get(random.nextInt(groupIdList.size())));

	}
}

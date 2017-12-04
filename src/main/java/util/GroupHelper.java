/**
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

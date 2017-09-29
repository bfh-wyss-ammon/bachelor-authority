package util;

import java.util.List;
import java.util.Random;

import data.DbGroup;

public class GroupHelper {
	
	public static DbGroup getGroup() {
		List<Integer> groupIdList = Database.Get(Integer.class, "groupId", DbGroup.class);
		Random random = new Random();
		
		return Database.Get(DbGroup.class, groupIdList.get(random.nextInt(groupIdList.size())));
		
	}
}

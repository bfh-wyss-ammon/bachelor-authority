package util;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import data.User;

public class DatabaseManagerTest {

	@Before
	public void Initialize() {

	}
	
	@Test
	public void GetTableName() {
		assertEquals("User", DatabaseManager.GetTableName(User.class));
	}

	@Test
	public void Get() {
		User u = DatabaseManager.Get(User.class, 1);
		assertNotNull(u);
	}
	
	@Test
	public void Save() {
		User u = new User("test@test.ch", "1212");
		u.setUserId(DatabaseManager.Save(Integer.class, u));
		
		assertNotNull(u.getUserId());
	}
	
	@Test
	public void Delete() {
		User u = new User("test@test.ch", "1212");
		u.setUserId(DatabaseManager.Save(Integer.class, u));
		assertNotNull(u.getUserId());
		
		DatabaseManager.Delete(u.getUserId(), User.class);
	}
	
	@Test
	public void GetMany() {
		List<User> users = DatabaseManager.Get(User.class);
		assertNotNull(users);
		assertEquals(users.size() > 0, true);
	}
	
	@Test
	public void AllSave() {
		User u = new User("test@test.ch", "1212");
		u.setUserId(DatabaseManager.Save(Integer.class, u));
		
		assertNotNull(u.getUserId());
	}
	
	@Test
	public void Update() {
		User u = DatabaseManager.Get(User.class, 1);
		assertNotNull(u);
		u.setMail("blubr");
		DatabaseManager.Update(u);
		User afterUpdate = DatabaseManager.Get(User.class, 1);
		
		assertEquals(afterUpdate.getMail(), "blubr");
		
	}

}

package util;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import data.User;

public class DatabaseTest {

	@Before
	public void Initialize() {

	}
	
	@Test
	public void GetTableName() {
		assertEquals("User", Database.GetTableName(User.class));
	}

	@Test
	public void Get() {
		User u = Database.Get(User.class, 1);
		assertNotNull(u);
	}
	
	@Test
	public void Save() {
		User u = new User("test@test.ch", "1212");
		u.setUserId(Database.Save(Integer.class, u));		
		assertNotNull(u.getUserId());
	}
	
	@Test
	public void Delete() {
		User u = new User("test@test.ch", "1212");
		u.setUserId(Database.Save(Integer.class, u));
		assertNotNull(u.getUserId());		
		Database.Delete(u.getUserId(), User.class);
	}
	
	@Test
	public void GetMany() {
		List<User> users = Database.Get(User.class);
		assertNotNull(users);
		assertEquals(users.size() > 0, true);
	}
	
	@Test
	public void AllSave() {
		User u = new User("test@test.ch", "1212");
		u.setUserId(Database.Save(Integer.class, u));
		
		assertNotNull(u.getUserId());
	}
	
	@Test
	public void Exists() {
		assertEquals(Database.Exists(User.class, "email = 'blubr'"), true);
	}
	
	@Test
	public void Update() {
		User u = Database.Get(User.class, 1);
		assertNotNull(u);
		//u.setMail("blubr");
		Database.Update(u);
		User afterUpdate = Database.Get(User.class, 1);
		
		//assertEquals(afterUpdate.getMail(), "blubr");
		
	}

}

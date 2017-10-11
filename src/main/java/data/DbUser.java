package data;

import com.google.gson.annotations.Expose;

public class DbUser {
	@Expose
	private Integer userId;
	@Expose
	private String id;
	@Expose
	private String lastname;
	@Expose
	private String firstname;
	@Expose(serialize = false, deserialize = true)
	private String password;

	public DbUser() {

	}

	public DbUser(String id, String password) {
		super();
		this.id = id;
		this.password = password;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

}

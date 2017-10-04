package data;

import com.google.gson.annotations.Expose;

public class DbUser {
	@Expose
	private Integer userId;

	@Expose
	private String mail;
	@Expose(serialize = false, deserialize = true)
	private String password;

	public DbUser() {

	}

	public DbUser(String mail, String password) {
		super();
		this.mail = mail;
		this.password = password;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

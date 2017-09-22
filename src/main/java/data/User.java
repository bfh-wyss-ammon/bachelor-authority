package data;

@Entity(dbTableName = "User")
public class User {

	private Integer userId;
	private String mail;
	private String password;

	public User() {

	}

	public User(String mail, String password) {
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

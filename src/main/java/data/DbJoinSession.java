package data;

import java.util.Date;

public class DbJoinSession {
	private int joinSessionId;
	private DbUser user;
	private String token;
	private Date created;

	public int getJoinSessionId() {
		return joinSessionId;
	}

	public void setJoinSessionId(int joinSessionId) {
		this.joinSessionId = joinSessionId;
	}

	public DbUser getUser() {
		return user;
	}

	public void setUser(DbUser user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}

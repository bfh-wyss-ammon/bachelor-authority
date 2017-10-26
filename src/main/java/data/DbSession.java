package data;

import java.util.Date;

public class DbSession {
	private int sessionId;
	private DbUser user;
	private String token;
	private Date created;

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int joinSessionId) {
		this.sessionId = joinSessionId;
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

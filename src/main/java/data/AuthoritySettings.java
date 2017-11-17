package data;

public class AuthoritySettings {

	private String salt;
	private int port;
	private int portalPort;
	private int joinSessionTimeout;
	private String token;

	public AuthoritySettings() {
		this.salt = "honolulu";
		this.port = 10000;
		this.portalPort = 10009;
		this.joinSessionTimeout = 1800;
		this.token = "7596b176-c54a-11e7-abc4-cec278b6b50a";
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPortalPort() {
		return portalPort;
	}

	public void setPortalPort(int portalPort) {
		this.portalPort = portalPort;
	}

	public int getJoinSessionTimeout() {
		return joinSessionTimeout;
	}

	public void setJoinSessionTimeout(int joinSessionTimeout) {
		this.joinSessionTimeout = joinSessionTimeout;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}

package data;

import com.google.gson.annotations.Expose;

import settings.Settings;

public class AuthoritySettings implements Settings {
	@Expose
	private int modulus;
	@Expose
	private int lE;
	@Expose
	private int lQ;
	@Expose
	private int lc;
	@Expose
	private int le;
	@Expose
	private int prime_certainty;
	private String salt;
	private int port;
	private String acceptedOrigin;
	


	public AuthoritySettings() {
		this.modulus = 2048;
		this.lE = 504;
		this.lQ = 282;
		this.lc = 160;
		this.le = 60;
		this.prime_certainty = 100;
		this.salt = "honolulu";
		this.port = 10000;
		this.acceptedOrigin = "http://*:*";
	}

	public int getModulus() {
		return modulus;
	}

	public void setModulus(int modulus) {
		this.modulus = modulus;
	}

	public int getlE() {
		return lE;
	}

	public void setlE(int lE) {
		this.lE = lE;
	}

	public int getlQ() {
		return lQ;
	}

	public void setlQ(int lQ) {
		this.lQ = lQ;
	}

	public int getlc() {
		return lc;
	}

	public void setLc(int lc) {
		this.lc = lc;
	}

	public int getle() {
		return le;
	}

	public void setLe(int le) {
		this.le = le;
	}

	public int getPrimeCertainty() {
		return prime_certainty;
	}

	public void setPrime_certainty(int prime_certainty) {
		this.prime_certainty = prime_certainty;
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

	public String getAcceptedOrigin() {
		return acceptedOrigin;
	}

	public void setAcceptedOrigin(String listenOn) {
		this.acceptedOrigin = listenOn;
	}

}

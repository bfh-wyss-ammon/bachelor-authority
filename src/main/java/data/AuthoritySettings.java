package data;

import settings.Settings;

public class AuthoritySettings implements Settings {
	private int modulus;
	private int lE;
	private int lQ;
	private int lc;
	private int le;
	private int prime_certainty;
	private String salt;

	public AuthoritySettings() {
		this.modulus = 2048;
		this.lE = 504;
		this.lQ = 282;
		this.lc = 160;
		this.le = 60;
		this.prime_certainty = 100;
		this.salt = "honolulu";
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

}
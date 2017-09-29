package data;

import java.io.Serializable;
import java.math.BigInteger;

import keys.ManagerKey;

public class DbManagerKey implements ManagerKey, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer managerKeyId;
	private BigInteger Xg;
	private BigInteger p;
	private BigInteger q;

	public BigInteger getXg() {
		return Xg;
	}

	public void setXg(BigInteger xg) {
		Xg = xg;
	}

	public BigInteger getP() {
		return p;
	}

	public void setP(BigInteger p) {
		this.p = p;
	}

	public BigInteger getQ() {
		return q;
	}

	public void setQ(BigInteger q) {
		this.q = q;
	}

	public Integer getManagerKeyId() {
		return managerKeyId;
	}

	public void setManagerKeyId(Integer managerKeyId) {
		this.managerKeyId = managerKeyId;
	}

}

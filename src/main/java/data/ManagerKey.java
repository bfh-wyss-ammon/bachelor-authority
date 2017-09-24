package data;

import java.math.BigInteger;

@Entity(dbTableName = "ManagerKey")
public class ManagerKey {
	private Integer managerKeyId;
	private BigInteger Xg;
	private BigInteger Xh;
	private BigInteger p;
	private BigInteger q;

	public Integer getManagerKeyId() {
		return managerKeyId;
	}

	public void setManagerKeyId(Integer managerKeyId) {
		this.managerKeyId = managerKeyId;
	}

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

	public BigInteger getProductFromPandQ() {
		return p.multiply(q);
	}

	public BigInteger getXh() {
		return Xh;
	}

	public void setXh(BigInteger xh) {
		Xh = xh;
	}
	
}

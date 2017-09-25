package data;

import java.math.BigInteger;

public class GroupJoinResponse {
	private BigInteger wi;
	private BigInteger yi;
	private BigInteger Ei;
	private BigInteger ri;
	private BigInteger e;

	public GroupJoinResponse() {
		super();
	}

	public BigInteger getWi() {
		return wi;
	}

	public void setWi(BigInteger wi) {
		this.wi = wi;
	}

	public BigInteger getYi() {
		return yi;
	}

	public void setYi(BigInteger yi) {
		this.yi = yi;
	}

	public BigInteger getEi() {
		return Ei;
	}

	public void setEi(BigInteger ei) {
		Ei = ei;
	}

	public BigInteger getRi() {
		return ri;
	}

	public void setRi(BigInteger ri) {
		this.ri = ri;
	}

	public BigInteger getE() {
		return e;
	}

	public void setE(BigInteger e) {
		this.e = e;
	}

}

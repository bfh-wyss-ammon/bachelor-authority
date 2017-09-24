package data;

import java.math.BigInteger;

@Entity(dbTableName = "MemberKey")
public class MemberKey {

	private Integer memberKeyId;
	private BigInteger x;
	private BigInteger w;
	private BigInteger y;
	private BigInteger e;
	private BigInteger r;
	private BigInteger bigE;
	private BigInteger bigY;
	private BigInteger commitment;

	public MemberKey() {

	}

	public Integer getMemberKeyId() {
		return memberKeyId;
	}

	public void setMemberKeyId(Integer memberKeyId) {
		this.memberKeyId = memberKeyId;
	}

	public BigInteger getX() {
		return x;
	}

	public void setX(BigInteger x) {
		this.x = x;
	}

	public BigInteger getW() {
		return w;
	}

	public void setW(BigInteger w) {
		this.w = w;
	}

	public BigInteger getY() {
		return y;
	}

	public void setY(BigInteger y) {
		this.y = y;
	}

	public BigInteger getE() {
		return e;
	}

	public void setE(BigInteger e) {
		this.e = e;
	}

	public BigInteger getR() {
		return r;
	}

	public void setR(BigInteger r) {
		this.r = r;
	}

	public BigInteger getBigE() {
		return bigE;
	}

	public void setBigE(BigInteger bigE) {
		this.bigE = bigE;
	}

	public BigInteger getBigY() {
		return bigY;
	}

	public void setBigY(BigInteger bigY) {
		this.bigY = bigY;
	}

	public BigInteger getCommitment() {
		return commitment;
	}

	public void setCommitment(BigInteger commitment) {
		this.commitment = commitment;
	}
}

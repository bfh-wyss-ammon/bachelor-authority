package data;

import java.math.BigInteger;

public class GroupJoinRequest {
	private BigInteger bigY;
	private BigInteger commitment;

	public GroupJoinRequest() {

	}

	public GroupJoinRequest(BigInteger bigY, BigInteger commitment) {
		super();
		this.bigY = bigY;
		this.commitment = commitment;
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

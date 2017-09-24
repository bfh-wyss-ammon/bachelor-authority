package data;

import java.math.BigInteger;

@Entity(dbTableName = "Membership")
public class Membership {
	private BigInteger bigY;
	private Integer userId;
	private Integer groupSignatureId;

	public BigInteger getBigY() {
		return bigY;
	}

	public void setBigY(BigInteger bigY) {
		this.bigY = bigY;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getGroupSignatureId() {
		return groupSignatureId;
	}

	public void setGroupSignatureId(Integer groupSignatureId) {
		this.groupSignatureId = groupSignatureId;
	}

}

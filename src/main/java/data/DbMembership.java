package data;

import java.math.BigInteger;

@Entity(dbTableName = "Membership")
public class DbMembership {

	private int membershipId;
	private DbUser user;
	private DbGroup group;
	private BigInteger bigY;
	private Boolean approved;

	public int getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(int membershipId) {
		this.membershipId = membershipId;
	}

	public DbUser getUser() {
		return user;
	}

	public void setUser(DbUser user) {
		this.user = user;
	}

	public DbGroup getGroup() {
		return group;
	}

	public void setGroup(DbGroup group) {
		this.group = group;
	}

	public BigInteger getBigY() {
		return bigY;
	}

	public void setBigY(BigInteger bigY) {
		this.bigY = bigY;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

}
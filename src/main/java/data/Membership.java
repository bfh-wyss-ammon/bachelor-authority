package data;

import java.math.BigInteger;

@Entity(dbTableName = "Membership")
public class Membership {
	private BigInteger bigY;
	private Integer MembershipId;
	private User user;
	private GroupKey groupKey;

	public Membership() {
		super();
	}

	public BigInteger getBigY() {
		return bigY;
	}

	public void setBigY(BigInteger bigY) {
		this.bigY = bigY;
	}

	public Integer getMembershipId() {
		return MembershipId;
	}

	public void setMembershipId(Integer membershipId) {
		MembershipId = membershipId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public GroupKey getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(GroupKey groupKey) {
		this.groupKey = groupKey;
	}

}

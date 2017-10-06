package data;

import java.math.BigInteger;
import java.util.Date;

import com.google.gson.annotations.Expose;

public class DbMembership {

	private int membershipId;
	@Expose
	private DbUser user;
	@Expose
	private DbGroup group;
	@Expose
	private BigInteger bigY;
	@Expose
	private Boolean approved;
	@Expose
	private Date created;

	
	
	public int getMembershipId() {
		return membershipId;
	}

	public DbMembership() {
		super();
	}
	
	

	public DbMembership(DbUser user, DbGroup group) {
		super();
		this.user = user;
		this.group = group;
		this.approved = false;
		this.created = new Date();
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
/**
 *   Copyright 2018 Pascal Ammon, Gabriel Wyss
 * 
 * 	 Implementation eines anonymen Mobility Pricing Systems auf Basis eines Gruppensignaturschemas
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This class stores the data related to the membership of a user in a signature group. This includes the identifying element of the member.
 */

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
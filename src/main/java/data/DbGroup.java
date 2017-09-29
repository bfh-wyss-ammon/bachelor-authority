package data;

import com.google.gson.annotations.Expose;

public class DbGroup {
	@Expose
	private Integer groupId;
	private DbManagerKey managerKey;
	@Expose
	private DbPublicKey publicKey;
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public DbManagerKey getManagerKey() {
		return managerKey;
	}
	public void setManagerKey(DbManagerKey managerKey) {
		this.managerKey = managerKey;
	}
	public DbPublicKey getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(DbPublicKey publicKey) {
		this.publicKey = publicKey;
	}
	
}

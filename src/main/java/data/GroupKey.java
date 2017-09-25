package data;

@Entity(dbTableName = "GroupKey")
public class GroupKey {
	private Integer groupKeyId;
	private ManagerKey managerKey;
	private PublicKey publicKey;

	public GroupKey() {

	}

	public GroupKey(ManagerKey managerKey, PublicKey publicKey) {
		super();
		this.managerKey = managerKey;
		this.publicKey = publicKey;
	}

	public ManagerKey getManagerKey() {
		return managerKey;
	}

	public void setManagerKey(ManagerKey managerKey) {
		this.managerKey = managerKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public Integer getGroupKeyId() {
		return groupKeyId;
	}

	public void setGroupKeyId(Integer groupKeyId) {
		this.groupKeyId = groupKeyId;
	}

}

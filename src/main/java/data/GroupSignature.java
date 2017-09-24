package data;

@Entity(dbTableName = "GroupSignature")
public class GroupSignature {
	private Integer groupSignatureId;
	private ManagerKey managerKey;
	private PublicKey publicKey;

	public ManagerKey getManagerKey() {
		return managerKey;
	}
	
	public GroupSignature() {
		
	}

	public GroupSignature(ManagerKey managerKey, PublicKey publicKey) {
		super();
		this.managerKey = managerKey;
		this.publicKey = publicKey;
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

	public Integer getGroupSignatureId() {
		return groupSignatureId;
	}

	public void setGroupSignatureId(Integer groupSignatureId) {
		this.groupSignatureId = groupSignatureId;
	}

}

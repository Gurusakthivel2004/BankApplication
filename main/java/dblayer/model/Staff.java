package dblayer.model;

public class Staff {
	
	private Long userID;
    private Long branchID;
	private Long createdAt;
    private Long modifiedAt;
    private Long performedBy;
    
    public Long getUserID() {
		return userID;
	}
    
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	
	public Long getBranchID() {
		return branchID;
	}
	
	public void setBranchID(Long branchID) {
		this.branchID = branchID;
	}
	
	public Long getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}
	
	public Long getModifiedAt() {
		return modifiedAt;
	}
	
	public void setModifiedAt(Long modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	
	public Long getPerformedBy() {
		return performedBy;
	}
	
	public void setPerformedBy(Long performedBy) {
		this.performedBy = performedBy;
	}
    
	@Override
	public String toString() {
		return "Staff [userID=" + userID + ", branchID=" + branchID + ", createdAt=" + createdAt + ", modifiedAt="
				+ modifiedAt + ", performedBy=" + performedBy + "]";
	}
}

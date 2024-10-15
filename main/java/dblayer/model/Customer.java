package dblayer.model;

import java.util.List;

public class Customer extends User {
	
    private Long userID;
    private String panNumber;
    private Long aadharNumber;
    private Long createdAt;
    private Long modifiedAt;
    private Long performedBy;
    
    public Customer() {}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public Long getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(Long aadharNumber) {
		this.aadharNumber = aadharNumber;
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
		return "Customer [userID=" + userID + ", panNumber=" + panNumber + ", aadharNumber=" + aadharNumber
				+ ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + ", performedBy=" + performedBy + "]";
	}
    
}

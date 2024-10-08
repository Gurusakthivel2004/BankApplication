package dblayer.model;

import java.util.List;

public class Customer {
	
    private Long userID;
    private String dob;
    private String fatherName;
    private String motherName;
    private String address;
    private String maritalStatus;
    private String panNumber;
    private Long aadharNumber;
    private Long createdAt;
    private Long modifiedAt;
    private Long performedBy;
    private List<Nominee> nominees;
    
    public Customer() {}

    public Customer(Long userID, String dob, String fatherName, String motherName, String address, String maritalStatus, String panNumber, Long aadharNumber, Long createdAt, Long modifiedAt, Long performedBy, List<Nominee>nominees ) {
        this.userID = userID;
    	this.dob = dob;
    	this.fatherName = fatherName;
    	this.motherName = motherName;
        this.address = address;
        this.maritalStatus = maritalStatus;
        this.panNumber = panNumber;
        this.aadharNumber = aadharNumber;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.performedBy = performedBy;
        this.nominees = nominees;
    }

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
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

	public List<Nominee> getNominees() {
		return nominees;
	}

	public void setNominees(List<Nominee> nominees) {
		this.nominees = nominees;
	}

	@Override
	public String toString() {
		return "Customer [id=" + userID + ", dob=" + dob + ", fatherName=" + fatherName + ", motherName=" + motherName
				+ ", address=" + address + ", maritalStatus=" + maritalStatus + ", panNumber=" + panNumber
				+ ", aadharNumber=" + aadharNumber + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt
				+ ", performedBy=" + performedBy + ", nominees=" + nominees + "]";
	}

    
}

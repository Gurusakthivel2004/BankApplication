package dblayer.model;

public class CustomerDetail extends Customer {
	
    private Long userID;
    private String dob;
    private String fatherName;
    private String motherName;
    private String address;
    private String maritalStatus;
    
    public CustomerDetail() {}

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

	@Override
	public String toString() {
		return "CustomerDetail [userID=" + userID + ", dob=" + dob + ", fatherName=" + fatherName + ", motherName="
				+ motherName + ", address=" + address + ", maritalStatus=" + maritalStatus + "]";
	}
    
}
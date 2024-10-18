package dblayer.model;

public class CustomerDetail extends Customer {
	
	private Long customerId;
    private String dob;
    private String fatherName;

	public CustomerDetail(String fullname, String email, Long phone, String role, String username,
			String password, String status, Long createdAt, Long modifiedAt, String panNumber, Long aadharNumber,
			Long performedBy, String dob, String fatherName, String motherName, String address, String maritalStatus) {
		super(fullname, email, phone, role, username, password, status, createdAt, modifiedAt, panNumber,
				aadharNumber, performedBy);
		this.dob = dob;
		this.fatherName = fatherName;
		this.motherName = motherName;
		this.address = address;
		this.maritalStatus = maritalStatus;
	}

	private String motherName;
    private String address;
    private String maritalStatus;
    
    public CustomerDetail() {}

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
		return super.toString() + "CustomerDetail [id=" + getId() + ", dob=" + dob + ", fatherName=" + fatherName + ", motherName="
				+ motherName + ", address=" + address + ", maritalStatus=" + maritalStatus + "]";
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
    
}
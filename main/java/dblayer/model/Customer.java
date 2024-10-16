package dblayer.model;

public class Customer extends User {
	
	private Long userId;
    private String panNumber;
    private Long aadharNumber;
	
    public Customer(String fullname, String email, Long phone, String role, String username, String password,
			String status, Long createdAt, Long modifiedAt, String panNumber, Long aadharNumber, Long performedBy) {
		super(fullname, email, phone, role, username, password, status, createdAt, modifiedAt, performedBy);
		this.panNumber = panNumber;
		this.aadharNumber = aadharNumber;
	}

    public Customer(Long userID, String panNumber, Long aadharNumber) {
		this.panNumber = panNumber;
		this.aadharNumber = aadharNumber;
    }
    
	public Customer() {}


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

	@Override
	public String toString() {
		return super.toString() + "Customer [id=" + getId() + ", panNumber=" + panNumber + ", aadharNumber=" + aadharNumber;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
    
}

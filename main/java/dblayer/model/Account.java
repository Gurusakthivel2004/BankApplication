package dblayer.model;

import java.math.BigDecimal;

public class Account implements Bank {
	
	enum AccountStatus {
	  Suspended,
	  Active,
	  Inactive
	}
	
	enum AccountType {
	  Savings,
	  Current,
	  Fixed_Deposit
	}
    
    private Long accountNumber;
    private Long branchId;
    private Long customerId;
    private AccountType accountType;
    private AccountStatus status;
    private BigDecimal balance;
    private BigDecimal minBalance;
    private Long createdAt;
    private Long modifiedAt;
    private Long performedBy;

    public Account() {}

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(BigDecimal minBalance) {
        this.minBalance = minBalance;
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
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", branchId=" + branchId +
                ", customerId=" + customerId +
                ", accountType=" + accountType +
                ", status=" + status +
                ", balance=" + balance +
                ", minBalance=" + minBalance +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", performedBy=" + performedBy +
                '}';
    }
}

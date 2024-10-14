package dblayer.model;

import java.math.BigDecimal;

public class Transaction {
	
	public enum TransactionType {
	    CREDIT,
	    DEBIT
	}
	
	public enum TransactionStatus {
	    PENDING,
	    COMPLETED,
	    FAILED,
	    CANCELLED
	}

    private Long id;
    private Long customerID;
    private Long accountNumber;
    private Long transactionAccountNumber;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String remarks;
    private BigDecimal amount;
    private BigDecimal closingBalance;
    private Long transactionTime;
    private Long performedBy;

    public Transaction() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerID;
    }

    public void setCustomerId(Long customerId) {
        this.customerID = customerId;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getTransactionAccountNumber() {
        return transactionAccountNumber;
    }

    public void setTransactionAccountNumber(Long transactionAccountNumber) {
        this.transactionAccountNumber = transactionAccountNumber;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }

    public Long getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Long transactionTime) {
        this.transactionTime = transactionTime;
    }

    public Long getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(Long performedBy) {
        this.performedBy = performedBy;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", customerId=" + customerID +
                ", accountNumber=" + accountNumber +
                ", transactionAccountNumber=" + transactionAccountNumber +
                ", transactionType=" + transactionType +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                ", amount=" + amount +
                ", closingBalance=" + closingBalance +
                ", transactionTime=" + transactionTime +
                ", performedBy=" + performedBy +
                '}';
    }
}

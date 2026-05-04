package com.aurapy.transaction;

import com.aurapy.security.CryptoUtil;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a single transaction in the AuraPay system
 */
public class Transaction implements Serializable, Comparable<Transaction> {
    private static final long serialVersionUID = 1L;

    private String transactionId;
    private String fromUserId;
    private String toUserId;
    private double amount;
    private TransactionStatus status;
    private String description;
    private LocalDateTime timestamp;
    private LocalDateTime processedAt;
    private String transactionHash;
    private String previousHash;
    private int nonce;
    private int confirmations;

    public Transaction() {}

    public Transaction(String fromUserId, String toUserId, double amount, String description) {
        this.transactionId = UUID.randomUUID().toString();
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.description = description;
        this.status = TransactionStatus.PENDING;
        this.timestamp = LocalDateTime.now();
        this.nonce = 0;
        this.confirmations = 0;
        this.transactionHash = calculateHash();
    }

    /**
     * Calculate SHA-256 hash of transaction
     * @return Transaction hash
     */
    public String calculateHash() {
        String input = transactionId + fromUserId + toUserId + amount + 
                      timestamp + nonce + description;
        return CryptoUtil.sha256Hash(input);
    }

    /**
     * Calculate transaction hash with previous hash (for blockchain)
     * @return New transaction hash
     */
    public String calculateHashWithPrevious() {
        String input = transactionId + fromUserId + toUserId + amount + 
                      timestamp + nonce + description + (previousHash != null ? previousHash : "");
        return CryptoUtil.sha256Hash(input);
    }

    /**
     * Perform proof of work mining
     * @param difficulty Number of leading zeros required
     */
    public void mineTransaction(int difficulty) {
        String target = "0".repeat(difficulty);
        while (!transactionHash.startsWith(target)) {
            nonce++;
            transactionHash = calculateHash();
        }
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public void incrementConfirmations() {
        this.confirmations++;
    }

    public boolean isConfirmed() {
        return confirmations >= 1;
    }

    @Override
    public int compareTo(Transaction other) {
        return this.timestamp.compareTo(other.timestamp);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", from='" + fromUserId + '\'' +
                ", to='" + toUserId + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                ", timestamp=" + timestamp +
                ", hash='" + transactionHash + '\'' +
                '}';
    }
}

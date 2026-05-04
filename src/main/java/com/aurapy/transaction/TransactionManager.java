package com.aurapy.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Manages all transactions in the AuraPay system
 */
public class TransactionManager {
    private static final Logger logger = LoggerFactory.getLogger(TransactionManager.class);

    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();
    private final Map<String, List<String>> userTransactions = new ConcurrentHashMap<>();
    private final TransactionValidator validator;
    private long totalProcessed = 0;
    private double totalVolume = 0;

    public TransactionManager(TransactionValidator validator) {
        this.validator = validator;
    }

    /**
     * Create a new transaction
     * @param fromUserId Sender user ID
     * @param toUserId Recipient user ID
     * @param amount Transaction amount
     * @param description Transaction description
     * @return Created Transaction
     */
    public Transaction createTransaction(String fromUserId, String toUserId, 
                                        double amount, String description) {
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Cannot send to yourself");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Transaction transaction = new Transaction(fromUserId, toUserId, amount, description);
        logger.info("Transaction created: {} from {} to {}", 
                   transaction.getTransactionId(), fromUserId, toUserId);
        return transaction;
    }

    /**
     * Execute/process a transaction
     * @param transaction Transaction to execute
     * @return True if successful, false otherwise
     */
    public boolean executeTransaction(Transaction transaction) {
        if (transaction == null) {
            logger.error("Transaction is null");
            return false;
        }

        if (!validator.validateTransaction(transaction)) {
            transaction.setStatus(TransactionStatus.FAILED);
            logger.warn("Transaction validation failed: {}", transaction.getTransactionId());
            return false;
        }

        transaction.setStatus(TransactionStatus.CONFIRMED);
        transaction.setProcessedAt(java.time.LocalDateTime.now());
        transaction.incrementConfirmations();
        
        transactions.put(transaction.getTransactionId(), transaction);
        
        // Record transaction for both users
        userTransactions.computeIfAbsent(transaction.getFromUserId(), k -> new ArrayList<>())
                       .add(transaction.getTransactionId());
        userTransactions.computeIfAbsent(transaction.getToUserId(), k -> new ArrayList<>())
                       .add(transaction.getTransactionId());

        totalProcessed++;
        totalVolume += transaction.getAmount();
        
        logger.info("Transaction executed: {} (Amount: {})", 
                   transaction.getTransactionId(), transaction.getAmount());
        return true;
    }

    /**
     * Get transaction by ID
     * @param transactionId Transaction ID
     * @return Transaction or null if not found
     */
    public Transaction getTransaction(String transactionId) {
        return transactions.get(transactionId);
    }

    /**
     * Get transaction history for a user
     * @param userId User ID
     * @return List of transaction IDs for the user
     */
    public List<Transaction> getTransactionHistory(String userId) {
        List<String> transactionIds = userTransactions.getOrDefault(userId, new ArrayList<>());
        return transactionIds.stream()
                .map(transactions::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Get received transactions for a user
     * @param userId User ID
     * @return List of received transactions
     */
    public List<Transaction> getReceivedTransactions(String userId) {
        return getTransactionHistory(userId).stream()
                .filter(t -> t.getToUserId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Get sent transactions for a user
     * @param userId User ID
     * @return List of sent transactions
     */
    public List<Transaction> getSentTransactions(String userId) {
        return getTransactionHistory(userId).stream()
                .filter(t -> t.getFromUserId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Verify transaction integrity
     * @param transactionId Transaction ID
     * @return True if transaction hash is valid, false otherwise
     */
    public boolean verifyTransaction(String transactionId) {
        Transaction transaction = transactions.get(transactionId);
        if (transaction == null) {
            return false;
        }
        String calculatedHash = transaction.calculateHash();
        return calculatedHash.equals(transaction.getTransactionHash());
    }

    /**
     * Cancel a pending transaction
     * @param transactionId Transaction ID
     * @return True if cancelled successfully, false otherwise
     */
    public boolean cancelTransaction(String transactionId) {
        Transaction transaction = transactions.get(transactionId);
        if (transaction == null) {
            return false;
        }
        if (transaction.getStatus() == TransactionStatus.PENDING) {
            transaction.setStatus(TransactionStatus.CANCELLED);
            logger.info("Transaction cancelled: {}", transactionId);
            return true;
        }
        return false;
    }

    /**
     * Get all transactions
     * @return List of all transactions
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions.values());
    }

    /**
     * Get transactions by status
     * @param status Transaction status
     * @return List of transactions with the specified status
     */
    public List<Transaction> getTransactionsByStatus(TransactionStatus status) {
        return transactions.values().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Get total transactions processed
     * @return Number of processed transactions
     */
    public long getTotalProcessed() {
        return totalProcessed;
    }

    /**
     * Get total transaction volume
     * @return Total amount transacted
     */
    public double getTotalVolume() {
        return totalVolume;
    }

    /**
     * Get pending transactions
     * @return List of pending transactions
     */
    public List<Transaction> getPendingTransactions() {
        return getTransactionsByStatus(TransactionStatus.PENDING);
    }

    /**
     * Get confirmed transactions
     * @return List of confirmed transactions
     */
    public List<Transaction> getConfirmedTransactions() {
        return getTransactionsByStatus(TransactionStatus.CONFIRMED);
    }
}

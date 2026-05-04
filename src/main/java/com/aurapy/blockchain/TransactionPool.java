package com.aurapy.blockchain;

import com.aurapy.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityQueue;

/**
 * Memory pool for pending transactions waiting to be mined
 */
public class TransactionPool implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(TransactionPool.class);

    private final Queue<Transaction> pendingTransactions = new CopyOnWriteArrayList<>();
    private final Set<String> transactionIds = Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<>());
    private final int maxPoolSize;
    private long totalRejected = 0;

    public TransactionPool(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    /**
     * Add transaction to the pool
     * @param transaction Transaction to add
     * @return True if added successfully
     */
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            logger.warn("Attempted to add null transaction");
            return false;
        }

        if (transactionIds.contains(transaction.getTransactionId())) {
            logger.warn("Transaction already in pool: {}", transaction.getTransactionId());
            return false;
        }

        if (pendingTransactions.size() >= maxPoolSize) {
            logger.warn("Transaction pool is full");
            totalRejected++;
            return false;
        }

        boolean added = pendingTransactions.add(transaction);
        if (added) {
            transactionIds.add(transaction.getTransactionId());
            logger.info("Transaction added to pool: {} (Pool size: {})", 
                       transaction.getTransactionId(), pendingTransactions.size());
            return true;
        }

        return false;
    }

    /**
     * Add multiple transactions to the pool
     * @param transactions Transactions to add
     * @return Number of successfully added transactions
     */
    public int addTransactions(List<Transaction> transactions) {
        int count = 0;
        for (Transaction tx : transactions) {
            if (addTransaction(tx)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Remove transaction from the pool
     * @param transactionId Transaction ID
     * @return True if removed
     */
    public boolean removeTransaction(String transactionId) {
        boolean removed = pendingTransactions.removeIf(tx -> tx.getTransactionId().equals(transactionId));
        if (removed) {
            transactionIds.remove(transactionId);
            logger.info("Transaction removed from pool: {}", transactionId);
        }
        return removed;
    }

    /**
     * Get pending transactions for mining
     * @param maxCount Maximum number of transactions
     * @return List of transactions
     */
    public List<Transaction> getTransactionsForMining(int maxCount) {
        List<Transaction> transactions = new ArrayList<>();
        int count = 0;
        
        for (Transaction tx : pendingTransactions) {
            if (count >= maxCount) break;
            transactions.add(tx);
            count++;
        }
        
        logger.info("Retrieved {} transactions for mining", count);
        return transactions;
    }

    /**
     * Clear transactions from pool (after mining)
     * @param transactions Transactions to clear
     */
    public void clearTransactions(List<Transaction> transactions) {
        for (Transaction tx : transactions) {
            removeTransaction(tx.getTransactionId());
        }
    }

    /**
     * Get all pending transactions
     * @return List of pending transactions
     */
    public List<Transaction> getPendingTransactions() {
        return new ArrayList<>(pendingTransactions);
    }

    /**
     * Get pool size
     * @return Number of pending transactions
     */
    public int getSize() {
        return pendingTransactions.size();
    }

    /**
     * Check if transaction exists in pool
     * @param transactionId Transaction ID
     * @return True if exists
     */
    public boolean containsTransaction(String transactionId) {
        return transactionIds.contains(transactionId);
    }

    /**
     * Clear all transactions from pool
     */
    public void clear() {
        pendingTransactions.clear();
        transactionIds.clear();
        logger.info("Transaction pool cleared");
    }

    /**
     * Get pool statistics
     * @return Statistics map
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("pendingCount", getSize());
        stats.put("maxSize", maxPoolSize);
        stats.put("utilizationPercent", (getSize() * 100.0) / maxPoolSize);
        stats.put("totalRejected", totalRejected);
        return stats;
    }

    /**
     * Get total rejected transactions
     * @return Rejection count
     */
    public long getTotalRejected() {
        return totalRejected;
    }
}

package com.aurapy.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates transactions for integrity and compliance
 */
public class TransactionValidator {
    private static final Logger logger = LoggerFactory.getLogger(TransactionValidator.class);

    private static final double MIN_AMOUNT = 0.01;
    private static final double MAX_AMOUNT = 100000.0;
    private static final int MIN_DESCRIPTION_LENGTH = 1;
    private static final int MAX_DESCRIPTION_LENGTH = 500;

    /**
     * Validate a transaction
     * @param transaction Transaction to validate
     * @return True if valid, false otherwise
     */
    public boolean validateTransaction(Transaction transaction) {
        if (transaction == null) {
            logger.warn("Transaction is null");
            return false;
        }

        if (!validateAmount(transaction.getAmount())) {
            logger.warn("Invalid amount: {}", transaction.getAmount());
            return false;
        }

        if (!validateUserIds(transaction.getFromUserId(), transaction.getToUserId())) {
            logger.warn("Invalid user IDs");
            return false;
        }

        if (!validateDescription(transaction.getDescription())) {
            logger.warn("Invalid description");
            return false;
        }

        if (!validateHash(transaction)) {
            logger.warn("Hash validation failed");
            return false;
        }

        return true;
    }

    /**
     * Validate transaction amount
     * @param amount Amount to validate
     * @return True if valid, false otherwise
     */
    public boolean validateAmount(double amount) {
        return amount >= MIN_AMOUNT && amount <= MAX_AMOUNT;
    }

    /**
     * Validate user IDs
     * @param fromUserId Sender ID
     * @param toUserId Recipient ID
     * @return True if valid, false otherwise
     */
    public boolean validateUserIds(String fromUserId, String toUserId) {
        return fromUserId != null && !fromUserId.isEmpty() &&
               toUserId != null && !toUserId.isEmpty() &&
               !fromUserId.equals(toUserId);
    }

    /**
     * Validate description
     * @param description Description to validate
     * @return True if valid, false otherwise
     */
    public boolean validateDescription(String description) {
        return description != null &&
               description.length() >= MIN_DESCRIPTION_LENGTH &&
               description.length() <= MAX_DESCRIPTION_LENGTH;
    }

    /**
     * Validate transaction hash
     * @param transaction Transaction to validate
     * @return True if hash is valid, false otherwise
     */
    public boolean validateHash(Transaction transaction) {
        String calculatedHash = transaction.calculateHash();
        return calculatedHash.equals(transaction.getTransactionHash());
    }

    /**
     * Check for double spending
     * @param userBalance User's current balance
     * @param transactionAmount Transaction amount
     * @return True if sufficient balance, false otherwise
     */
    public boolean checkSufficientBalance(double userBalance, double transactionAmount) {
        return userBalance >= transactionAmount;
    }
}

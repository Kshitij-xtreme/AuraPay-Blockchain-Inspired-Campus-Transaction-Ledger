package com.aurapy.api;

import com.aurapy.AuraPay;
import com.aurapy.blockchain.Block;
import com.aurapy.blockchain.Blockchain;
import com.aurapy.transaction.Transaction;
import com.aurapy.transaction.TransactionStatus;
import com.aurapy.user.User;
import com.aurapy.user.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * REST-like API for AuraPay Ledger
 */
public class LedgerAPI {
    private static final Logger logger = LoggerFactory.getLogger(LedgerAPI.class);
    private final AuraPay auraPay;

    public LedgerAPI(AuraPay auraPay) {
        this.auraPay = auraPay;
    }

    // ===================== USER ENDPOINTS =====================

    /**
     * Create new user
     */
    public Map<String, Object> createUser(String name, String email, String role) {
        try {
            User user = auraPay.getUserManager().createUser(name, email, role);
            return createSuccessResponse("User created successfully", user);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Get user details
     */
    public Map<String, Object> getUser(String userId) {
        try {
            User user = auraPay.getUserManager().getUser(userId);
            if (user == null) {
                return createErrorResponse("User not found");
            }
            return createSuccessResponse("User retrieved", user);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Update user
     */
    public Map<String, Object> updateUser(String userId, String name, String department, String studentId) {
        try {
            User user = auraPay.getUserManager().updateUser(userId, name, department, studentId);
            return createSuccessResponse("User updated successfully", user);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Get user balance
     */
    public Map<String, Object> getBalance(String userId) {
        try {
            double balance = auraPay.getUserManager().getBalance(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("balance", balance);
            return createSuccessResponse("Balance retrieved", data);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Add balance
     */
    public Map<String, Object> addBalance(String userId, double amount) {
        try {
            auraPay.getUserManager().addBalance(userId, amount);
            double newBalance = auraPay.getUserManager().getBalance(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("amountAdded", amount);
            data.put("newBalance", newBalance);
            return createSuccessResponse("Balance added successfully", data);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Get all users
     */
    public Map<String, Object> getAllUsers() {
        try {
            List<User> users = auraPay.getUserManager().getAllUsers();
            Map<String, Object> data = new HashMap<>();
            data.put("totalUsers", users.size());
            data.put("users", users);
            return createSuccessResponse("Users retrieved", data);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    // ===================== TRANSACTION ENDPOINTS =====================

    /**
     * Create transaction
     */
    public Map<String, Object> createTransaction(String fromUserId, String toUserId, 
                                                   double amount, String description) {
        try {
            // Validate users exist
            if (auraPay.getUserManager().getUser(fromUserId) == null) {
                return createErrorResponse("Sender user not found");
            }
            if (auraPay.getUserManager().getUser(toUserId) == null) {
                return createErrorResponse("Recipient user not found");
            }

            // Check balance
            if (!auraPay.getUserManager().deductBalance(fromUserId, amount)) {
                return createErrorResponse("Insufficient balance");
            }

            // Add to recipient
            auraPay.getUserManager().addBalance(toUserId, amount);

            // Create and execute transaction
            Transaction transaction = auraPay.getTransactionManager()
                    .createTransaction(fromUserId, toUserId, amount, description);
            
            if (auraPay.getTransactionManager().executeTransaction(transaction)) {
                return createSuccessResponse("Transaction created successfully", transaction);
            } else {
                return createErrorResponse("Transaction execution failed");
            }
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Get transaction
     */
    public Map<String, Object> getTransaction(String transactionId) {
        try {
            Transaction tx = auraPay.getTransactionManager().getTransaction(transactionId);
            if (tx == null) {
                return createErrorResponse("Transaction not found");
            }
            return createSuccessResponse("Transaction retrieved", tx);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Get transaction history
     */
    public Map<String, Object> getTransactionHistory(String userId) {
        try {
            List<Transaction> transactions = auraPay.getTransactionManager()
                    .getTransactionHistory(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("totalTransactions", transactions.size());
            data.put("transactions", transactions);
            return createSuccessResponse("Transaction history retrieved", data);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Get all transactions
     */
    public Map<String, Object> getAllTransactions() {
        try {
            List<Transaction> transactions = auraPay.getTransactionManager()
                    .getAllTransactions();
            Map<String, Object> data = new HashMap<>();
            data.put("totalTransactions", transactions.size());
            data.put("transactions", transactions);
            return createSuccessResponse("All transactions retrieved", data);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    // ===================== BLOCKCHAIN ENDPOINTS =====================

    /**
     * Mine new block
     */
    public Map<String, Object> mineBlock(String minerAddress) {
        try {
            Block block = auraPay.mineNewBlock(minerAddress);
            if (block != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("blockIndex", block.getIndex());
                data.put("blockHash", block.getBlockHash());
                data.put("transactionCount", block.getTransactionCount());
                data.put("miningTime", block.getMiningTime() + "ms");
                return createSuccessResponse("Block mined successfully", data);
            }
            return createErrorResponse("Failed to mine block");
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Get blockchain info
     */
    public Map<String, Object> getBlockchainInfo() {
        try {
            Blockchain blockchain = auraPay.getBlockchain();
            return createSuccessResponse("Blockchain info retrieved", blockchain.getStatistics());
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Validate blockchain
     */
    public Map<String, Object> validateBlockchain() {
        try {
            boolean isValid = auraPay.getBlockchain().isChainValid();
            Map<String, Object> data = new HashMap<>();
            data.put("isValid", isValid);
            String message = isValid ? "Blockchain is valid" : "Blockchain is invalid";
            return createSuccessResponse(message, data);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Get block
     */
    public Map<String, Object> getBlock(int index) {
        try {
            Block block = auraPay.getBlockchain().getBlock(index);
            if (block == null) {
                return createErrorResponse("Block not found");
            }
            return createSuccessResponse("Block retrieved", block);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    // ===================== STATISTICS ENDPOINTS =====================

    /**
     * Get system statistics
     */
    public Map<String, Object> getSystemStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", auraPay.getUserManager().getTotalUsers());
            stats.put("totalTransactions", auraPay.getTransactionManager().getTotalProcessed());
            stats.put("totalVolume", auraPay.getTransactionManager().getTotalVolume());
            stats.put("blockchainSize", auraPay.getBlockchain().getSize());
            stats.put("blockchainValid", auraPay.getBlockchain().isChainValid());
            stats.put("pendingTransactions", auraPay.getTransactionPool().getSize());
            return createSuccessResponse("System statistics retrieved", stats);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    // ===================== HELPER METHODS =====================

    /**
     * Create success response
     */
    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", new Date());
        return response;
    }

    /**
     * Create error response
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", new Date());
        return response;
    }
}

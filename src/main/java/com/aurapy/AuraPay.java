package com.aurapy;

import com.aurapy.blockchain.Block;
import com.aurapy.blockchain.Blockchain;
import com.aurapy.blockchain.TransactionPool;
import com.aurapy.transaction.Transaction;
import com.aurapy.transaction.TransactionManager;
import com.aurapy.transaction.TransactionValidator;
import com.aurapy.user.UserManager;
import com.aurapy.api.LedgerAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Main AuraPay ledger system
 */
public class AuraPay {
    private static final Logger logger = LoggerFactory.getLogger(AuraPay.class);

    private final UserManager userManager;
    private final TransactionManager transactionManager;
    private final TransactionValidator transactionValidator;
    private final Blockchain blockchain;
    private final TransactionPool transactionPool;
    private final LedgerAPI api;

    private static final int BLOCKCHAIN_DIFFICULTY = 3;
    private static final int TRANSACTION_POOL_SIZE = 1000;
    private static final int MAX_TRANSACTIONS_PER_BLOCK = 100;

    public AuraPay() {
        logger.info("Initializing AuraPay Ledger System...");

        // Initialize components
        this.userManager = new UserManager();
        this.transactionValidator = new TransactionValidator();
        this.transactionManager = new TransactionManager(transactionValidator);
        this.blockchain = new Blockchain(BLOCKCHAIN_DIFFICULTY);
        this.transactionPool = new TransactionPool(TRANSACTION_POOL_SIZE);
        this.api = new LedgerAPI(this);

        logger.info("AuraPay Ledger System initialized successfully");
    }

    /**
     * Mine new block with pending transactions
     * @param minerAddress Address of the miner
     * @return Created block
     */
    public Block mineNewBlock(String minerAddress) {
        logger.info("Mining new block by: {}", minerAddress);

        List<Transaction> pendingTransactions = transactionPool
                .getTransactionsForMining(MAX_TRANSACTIONS_PER_BLOCK);

        if (pendingTransactions.isEmpty()) {
            logger.warn("No pending transactions to mine");
            return null;
        }

        Block newBlock = blockchain.createNewBlock(pendingTransactions, minerAddress);
        
        if (newBlock != null) {
            transactionPool.clearTransactions(pendingTransactions);
            logger.info("Block #{} mined successfully with {} transactions", 
                       newBlock.getIndex(), pendingTransactions.size());
        }

        return newBlock;
    }

    /**
     * Submit transaction to pool
     * @param transaction Transaction to submit
     * @return True if submitted successfully
     */
    public boolean submitTransaction(Transaction transaction) {
        if (transactionValidator.validateTransaction(transaction)) {
            return transactionPool.addTransaction(transaction);
        }
        return false;
    }

    /**
     * Process pending transaction
     * @param transaction Transaction to process
     * @return True if processed successfully
     */
    public boolean processTransaction(Transaction transaction) {
        return transactionManager.executeTransaction(transaction);
    }

    // Getters
    public UserManager getUserManager() {
        return userManager;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public TransactionValidator getTransactionValidator() {
        return transactionValidator;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public TransactionPool getTransactionPool() {
        return transactionPool;
    }

    public LedgerAPI getAPI() {
        return api;
    }

    /**
     * Get system status
     * @return Status string
     */
    public String getStatus() {
        return String.format(
            "AuraPay Status:\n" +
            "  Users: %d\n" +
            "  Transactions: %d\n" +
            "  Blockchain Size: %d blocks\n" +
            "  Pending Transactions: %d\n" +
            "  Blockchain Valid: %s\n" +
            "  Total Volume: %.2f",
            userManager.getTotalUsers(),
            transactionManager.getTotalProcessed(),
            blockchain.getSize(),
            transactionPool.getSize(),
            blockchain.isChainValid(),
            transactionManager.getTotalVolume()
        );
    }
}

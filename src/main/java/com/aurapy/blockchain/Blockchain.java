package com.aurapy.blockchain;

import com.aurapy.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents the blockchain ledger
 */
public class Blockchain implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Blockchain.class);

    private final List<Block> chain = new CopyOnWriteArrayList<>();
    private int difficulty;
    private long totalMiningTime = 0;
    private int totalBlocksMined = 0;

    public Blockchain(int difficulty) {
        this.difficulty = difficulty;
        createGenesisBlock();
    }

    /**
     * Create genesis block (first block)
     */
    private void createGenesisBlock() {
        Block genesisBlock = new Block(0, "0", difficulty);
        genesisBlock.setBlockHash(genesisBlock.calculateHash());
        genesisBlock.setMinerAddress("SYSTEM");
        chain.add(genesisBlock);
        logger.info("Genesis block created");
    }

    /**
     * Get the last block in the chain
     * @return Last block
     */
    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }

    /**
     * Add new block to the chain
     * @param block Block to add
     * @return True if added successfully
     */
    public boolean addBlock(Block block) {
        if (block == null) {
            return false;
        }

        Block lastBlock = getLastBlock();
        if (!block.getPreviousHash().equals(lastBlock.getBlockHash())) {
            logger.error("Invalid previous hash for block {}", block.getIndex());
            return false;
        }

        block.setIndex(chain.size());
        block.setBlockHash(block.calculateHash());
        block.mineBlock();

        chain.add(block);
        totalMiningTime += block.getMiningTime();
        totalBlocksMined++;

        logger.info("Block #{} added to chain (mining time: {}ms)", 
                   block.getIndex(), block.getMiningTime());
        return true;
    }

    /**
     * Create and add a new block with transactions
     * @param transactions Transactions for the block
     * @param minerAddress Miner's address
     * @return Created block
     */
    public Block createNewBlock(List<Transaction> transactions, String minerAddress) {
        Block newBlock = new Block(chain.size(), getLastBlock().getBlockHash(), difficulty);
        
        for (Transaction tx : transactions) {
            newBlock.addTransaction(tx);
        }
        
        newBlock.setMinerAddress(minerAddress);
        newBlock.setBlockHash(newBlock.calculateHash());
        
        if (addBlock(newBlock)) {
            return newBlock;
        }
        return null;
    }

    /**
     * Validate the entire blockchain
     * @return True if blockchain is valid
     */
    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Check if current block's hash is correct
            if (!currentBlock.getBlockHash().equals(currentBlock.calculateHash())) {
                logger.error("Invalid hash at block {}", i);
                return false;
            }

            // Check if link to previous block is correct
            if (!currentBlock.getPreviousHash().equals(previousBlock.getBlockHash())) {
                logger.error("Invalid previous hash at block {}", i);
                return false;
            }

            // Check proof of work
            String target = "0".repeat(difficulty);
            if (!currentBlock.getBlockHash().startsWith(target)) {
                logger.error("Invalid proof of work at block {}", i);
                return false;
            }
        }
        return true;
    }

    /**
     * Get block by index
     * @param index Block index
     * @return Block or null if not found
     */
    public Block getBlock(int index) {
        if (index >= 0 && index < chain.size()) {
            return chain.get(index);
        }
        return null;
    }

    /**
     * Get block by hash
     * @param hash Block hash
     * @return Block or null if not found
     */
    public Block getBlockByHash(String hash) {
        return chain.stream()
                .filter(b -> b.getBlockHash().equals(hash))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all blocks
     * @return List of all blocks
     */
    public List<Block> getAllBlocks() {
        return new ArrayList<>(chain);
    }

    /**
     * Get blockchain size
     * @return Number of blocks
     */
    public int getSize() {
        return chain.size();
    }

    /**
     * Get total transactions in blockchain
     * @return Transaction count
     */
    public int getTotalTransactions() {
        return chain.stream()
                .mapToInt(Block::getTransactionCount)
                .sum();
    }

    /**
     * Get difficulty level
     * @return Current difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Set difficulty level
     * @param difficulty New difficulty
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Get average mining time
     * @return Average time in milliseconds
     */
    public long getAverageMiningTime() {
        if (totalBlocksMined == 0) return 0;
        return totalMiningTime / totalBlocksMined;
    }

    /**
     * Get total mining time
     * @return Total time in milliseconds
     */
    public long getTotalMiningTime() {
        return totalMiningTime;
    }

    /**
     * Get total blocks mined
     * @return Block count
     */
    public int getTotalBlocksMined() {
        return totalBlocksMined;
    }

    /**
     * Get blockchain statistics
     * @return Map of statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBlocks", getSize());
        stats.put("totalTransactions", getTotalTransactions());
        stats.put("difficulty", difficulty);
        stats.put("totalMiningTime", totalMiningTime);
        stats.put("averageMiningTime", getAverageMiningTime());
        stats.put("isValid", isChainValid());
        return stats;
    }

    @Override
    public String toString() {
        return "Blockchain{" +
                "size=" + chain.size() +
                ", difficulty=" + difficulty +
                ", totalTransactions=" + getTotalTransactions() +
                ", isValid=" + isChainValid() +
                '}';
    }
}

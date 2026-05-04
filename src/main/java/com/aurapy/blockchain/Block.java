package com.aurapy.blockchain;

import com.aurapy.security.CryptoUtil;
import com.aurapy.transaction.Transaction;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a block in the blockchain
 */
public class Block implements Serializable, Comparable<Block> {
    private static final long serialVersionUID = 1L;

    private int index;
    private String blockHash;
    private String previousHash;
    private LocalDateTime timestamp;
    private List<Transaction> transactions;
    private int nonce;
    private int difficulty;
    private String minerAddress;
    private long miningTime;

    public Block() {
        this.transactions = new ArrayList<>();
    }

    public Block(int index, String previousHash, int difficulty) {
        this();
        this.index = index;
        this.previousHash = previousHash;
        this.timestamp = LocalDateTime.now();
        this.difficulty = difficulty;
        this.nonce = 0;
        this.miningTime = 0;
    }

    /**
     * Calculate SHA-256 hash of the block
     * @return Block hash
     */
    public String calculateHash() {
        String input = index + previousHash + timestamp + 
                      transactions.stream().map(Transaction::getTransactionHash)
                                  .reduce("", String::concat) + nonce;
        return CryptoUtil.sha256Hash(input);
    }

    /**
     * Mine the block using Proof of Work
     */
    public void mineBlock() {
        long startTime = System.currentTimeMillis();
        String target = "0".repeat(difficulty);
        
        while (!blockHash.startsWith(target)) {
            nonce++;
            blockHash = calculateHash();
        }
        
        miningTime = System.currentTimeMillis() - startTime;
    }

    /**
     * Mine block asynchronously
     * @param callback Callback when mining completes
     */
    public void mineBlockAsync(Runnable callback) {
        new Thread(() -> {
            mineBlock();
            if (callback != null) {
                callback.run();
            }
        }).start();
    }

    /**
     * Add transaction to block
     * @param transaction Transaction to add
     * @return True if added successfully
     */
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        return transactions.add(transaction);
    }

    /**
     * Get Merkle root of transactions
     * @return Merkle root hash
     */
    public String getMerkleRoot() {
        if (transactions.isEmpty()) {
            return "";
        }
        
        List<String> hashes = new ArrayList<>();
        for (Transaction tx : transactions) {
            hashes.add(tx.getTransactionHash());
        }
        
        while (hashes.size() > 1) {
            List<String> newHashes = new ArrayList<>();
            for (int i = 0; i < hashes.size(); i += 2) {
                String hash1 = hashes.get(i);
                String hash2 = (i + 1 < hashes.size()) ? hashes.get(i + 1) : hash1;
                newHashes.add(CryptoUtil.sha256Hash(hash1 + hash2));
            }
            hashes = newHashes;
        }
        
        return hashes.get(0);
    }

    // Getters and Setters
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getMinerAddress() {
        return minerAddress;
    }

    public void setMinerAddress(String minerAddress) {
        this.minerAddress = minerAddress;
    }

    public long getMiningTime() {
        return miningTime;
    }

    public void setMiningTime(long miningTime) {
        this.miningTime = miningTime;
    }

    public int getTransactionCount() {
        return transactions.size();
    }

    @Override
    public int compareTo(Block other) {
        return Integer.compare(this.index, other.index);
    }

    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", hash='" + blockHash + '\'' +
                ", prevHash='" + previousHash + '\'' +
                ", timestamp=" + timestamp +
                ", txCount=" + transactions.size() +
                ", nonce=" + nonce +
                ", miningTime=" + miningTime + "ms" +
                '}';
    }
}

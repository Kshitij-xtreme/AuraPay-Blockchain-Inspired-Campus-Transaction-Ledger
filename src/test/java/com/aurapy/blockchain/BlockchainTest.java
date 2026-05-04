package com.aurapy.blockchain;

import com.aurapy.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for Blockchain class
 */
public class BlockchainTest {

    private Blockchain blockchain;

    @Before
    public void setUp() {
        blockchain = new Blockchain(2);
    }

    @Test
    public void testBlockchainCreation() {
        assertNotNull(blockchain);
        assertEquals(1, blockchain.getSize());
    }

    @Test
    public void testBlockchainValidation() {
        assertTrue(blockchain.isChainValid());
    }

    @Test
    public void testAddBlock() {
        Block block = new Block(1, blockchain.getLastBlock().getBlockHash(), 2);
        Transaction tx = new Transaction("USER_1", "USER_2", 50.0, "Test");
        block.addTransaction(tx);
        block.setBlockHash(block.calculateHash());

        assertTrue(blockchain.addBlock(block));
        assertEquals(2, blockchain.getSize());
    }

    @Test
    public void testBlockchainStats() {
        assertNotNull(blockchain.getStatistics());
        assertTrue(blockchain.getStatistics().containsKey("totalBlocks"));
    }
}

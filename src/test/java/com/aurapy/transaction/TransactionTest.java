package com.aurapy.transaction;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for Transaction class
 */
public class TransactionTest {

    private Transaction transaction;

    @Before
    public void setUp() {
        transaction = new Transaction("USER_1", "USER_2", 100.0, "Test transaction");
    }

    @Test
    public void testTransactionCreation() {
        assertNotNull(transaction);
        assertEquals("USER_1", transaction.getFromUserId());
        assertEquals("USER_2", transaction.getToUserId());
        assertEquals(100.0, transaction.getAmount(), 0.01);
    }

    @Test
    public void testTransactionHash() {
        String hash = transaction.calculateHash();
        assertNotNull(hash);
        assertTrue(hash.length() > 0);
        assertEquals(hash, transaction.calculateHash());
    }

    @Test
    public void testTransactionStatus() {
        transaction.setStatus(TransactionStatus.CONFIRMED);
        assertEquals(TransactionStatus.CONFIRMED, transaction.getStatus());
    }

    @Test
    public void testTransactionMining() {
        transaction.mineTransaction(2);
        assertTrue(transaction.getTransactionHash().startsWith("00"));
    }
}

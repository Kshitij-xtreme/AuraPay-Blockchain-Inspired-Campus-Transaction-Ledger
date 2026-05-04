package com.aurapy.user;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for UserManager class
 */
public class UserManagerTest {

    private UserManager userManager;

    @Before
    public void setUp() {
        userManager = new UserManager();
    }

    @Test
    public void testCreateUser() {
        User user = userManager.createUser("John Doe", "john@campus.edu", "STUDENT");
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals(UserRole.STUDENT, user.getRole());
    }

    @Test
    public void testGetUser() {
        User user = userManager.createUser("Jane Doe", "jane@campus.edu", "FACULTY");
        User retrieved = userManager.getUser(user.getId());
        assertNotNull(retrieved);
        assertEquals(user.getId(), retrieved.getId());
    }

    @Test
    public void testAddBalance() {
        User user = userManager.createUser("Bob", "bob@campus.edu", "STUDENT");
        userManager.addBalance(user.getId(), 1000.0);
        assertEquals(1000.0, userManager.getBalance(user.getId()), 0.01);
    }

    @Test
    public void testGetAllUsers() {
        userManager.createUser("User1", "user1@campus.edu", "STUDENT");
        userManager.createUser("User2", "user2@campus.edu", "FACULTY");
        assertEquals(2, userManager.getAllUsers().size());
    }
}

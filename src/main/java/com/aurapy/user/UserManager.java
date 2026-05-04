package com.aurapy.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Manages user accounts in the AuraPay system
 */
public class UserManager {
    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);
    
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, String> emailToUserId = new ConcurrentHashMap<>();
    private int userCounter = 1000;

    /**
     * Create a new user
     * @param name User's full name
     * @param email User's email
     * @param role User's role
     * @return Created User object
     */
    public User createUser(String name, String email, String role) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        if (email == null || !isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address");
        }
        if (emailToUserId.containsKey(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        String userId = "USER_" + (++userCounter);
        UserRole userRole = UserRole.fromString(role);
        User user = new User(userId, name, email, userRole);
        
        users.put(userId, user);
        emailToUserId.put(email, userId);
        
        logger.info("User created: {} ({})", userId, email);
        return user;
    }

    /**
     * Get user by ID
     * @param userId User ID
     * @return User object or null if not found
     */
    public User getUser(String userId) {
        return users.get(userId);
    }

    /**
     * Get user by email
     * @param email User email
     * @return User object or null if not found
     */
    public User getUserByEmail(String email) {
        String userId = emailToUserId.get(email);
        return userId != null ? users.get(userId) : null;
    }

    /**
     * Update user information
     * @param userId User ID
     * @param name New name
     * @param department Department
     * @param studentId Student ID
     * @return Updated User object
     */
    public User updateUser(String userId, String name, String department, String studentId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        
        if (name != null && !name.trim().isEmpty()) {
            user.setName(name);
        }
        if (department != null && !department.trim().isEmpty()) {
            user.setDepartment(department);
        }
        if (studentId != null && !studentId.trim().isEmpty()) {
            user.setStudentId(studentId);
        }
        
        logger.info("User updated: {}", userId);
        return user;
    }

    /**
     * Set user password
     * @param userId User ID
     * @param password New password
     */
    public void setPassword(String userId, String password) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        user.setPassword(password);
        logger.info("Password set for user: {}", userId);
    }

    /**
     * Authenticate user
     * @param email User email
     * @param password Password
     * @return User object if authentication successful, null otherwise
     */
    public User authenticate(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null && user.isActive() && user.verifyPassword(password)) {
            user.setLastLogin(java.time.LocalDateTime.now());
            logger.info("User authenticated: {}", email);
            return user;
        }
        logger.warn("Authentication failed for: {}", email);
        return null;
    }

    /**
     * Add balance to user account
     * @param userId User ID
     * @param amount Amount to add
     */
    public void addBalance(String userId, double amount) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        user.addBalance(amount);
        logger.info("Balance added to {}: {} (New balance: {})", userId, amount, user.getBalance());
    }

    /**
     * Deduct balance from user account
     * @param userId User ID
     * @param amount Amount to deduct
     * @return True if successful, false otherwise
     */
    public boolean deductBalance(String userId, double amount) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return user.deductBalance(amount);
    }

    /**
     * Get user balance
     * @param userId User ID
     * @return User balance
     */
    public double getBalance(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return user.getBalance();
    }

    /**
     * Deactivate user account
     * @param userId User ID
     */
    public void deactivateUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        user.setActive(false);
        logger.info("User deactivated: {}", userId);
    }

    /**
     * Activate user account
     * @param userId User ID
     */
    public void activateUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        user.setActive(true);
        logger.info("User activated: {}", userId);
    }

    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Get users by role
     * @param role User role
     * @return List of users with the specified role
     */
    public List<User> getUsersByRole(UserRole role) {
        return users.values().stream()
                .filter(u -> u.getRole() == role)
                .collect(Collectors.toList());
    }

    /**
     * Get active users
     * @return List of active users
     */
    public List<User> getActiveUsers() {
        return users.values().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Get total user count
     * @return Number of users
     */
    public int getTotalUsers() {
        return users.size();
    }

    /**
     * Validate email format
     * @param email Email to validate
     * @return True if valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}

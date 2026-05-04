package com.aurapy.user;

import com.aurapy.security.CryptoUtil;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a user in the AuraPay system
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String email;
    private String studentId; // Campus ID
    private String department;
    private UserRole role;
    private double balance;
    private byte[] passwordSalt;
    private String passwordHash;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private int transactionCount;

    public User() {}

    public User(String id, String name, String email, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.balance = 0.0;
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.transactionCount = 0;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = Math.max(0, balance);
    }

    public void addBalance(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public boolean deductBalance(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        this.passwordSalt = CryptoUtil.generateSalt();
        this.passwordHash = CryptoUtil.hashPassword(password, this.passwordSalt);
    }

    public boolean verifyPassword(String password) {
        if (this.passwordHash == null || this.passwordSalt == null) {
            return false;
        }
        return CryptoUtil.verifyPassword(password, this.passwordSalt, this.passwordHash);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void incrementTransactionCount() {
        this.transactionCount++;
    }

    public void setTransactionCount(int count) {
        this.transactionCount = count;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", balance=" + balance +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", transactionCount=" + transactionCount +
                '}';
    }
}

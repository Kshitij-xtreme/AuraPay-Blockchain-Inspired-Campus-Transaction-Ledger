package com.aurapy.user;

/**
 * Enum for user roles in the AuraPay system
 */
public enum UserRole {
    STUDENT("Student", 1),
    STAFF("Staff", 2),
    FACULTY("Faculty", 3),
    ADMIN("Administrator", 4),
    MERCHANT("Merchant", 5);

    private final String displayName;
    private final int level;

    UserRole(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    public static UserRole fromString(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return STUDENT; // Default role
        }
    }

    public boolean canInitiateTransaction() {
        return this != ADMIN;
    }

    public boolean canApproveTransaction() {
        return this == ADMIN || this == FACULTY;
    }
}

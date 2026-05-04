package com.aurapy.transaction;

/**
 * Enum representing transaction status
 */
public enum TransactionStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    CANCELLED("Cancelled"),
    REJECTED("Rejected");

    private final String displayName;

    TransactionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TransactionStatus fromString(String status) {
        try {
            return TransactionStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }

    public boolean isFinal() {
        return this == COMPLETED || this == FAILED || this == REJECTED || this == CANCELLED;
    }
}

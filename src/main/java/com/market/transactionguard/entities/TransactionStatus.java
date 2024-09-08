package com.market.transactionguard.entities;

public enum TransactionStatus {
    PENDING, // Initial state when the transaction is created
    SUCCESSFUL, // When the transaction is completed successfully
    FAILED, // If the transaction fails for any reason (e.g., payment failure)
    DISPUTED, // If there is dispute between buyer and seller
    CANCELLED,  // If the buyer or seller cancels the transaction
    ON_HOLD // Transaction is put on hold for some reason (e.g., verification)
}

# AuraPay Development Guide

## Architecture Overview

AuraPay is built with a modular architecture consisting of:

### Core Modules

1. **User Module** (`com.aurapy.user`)
   - User account management
   - Role-based access control
   - Balance tracking and management

2. **Transaction Module** (`com.aurapy.transaction`)
   - Transaction creation and validation
   - Transaction status management
   - Transaction history tracking

3. **Blockchain Module** (`com.aurapy.blockchain`)
   - Block creation and mining
   - Blockchain validation
   - Merkle tree implementation
   - Transaction pool management

4. **Security Module** (`com.aurapy.security`)
   - Cryptographic operations (SHA-256)
   - Digital signatures (RSA)
   - Password hashing (PBKDF2)
   - Token generation

5. **Storage Module** (`com.aurapy.storage`)
   - Data persistence
   - File-based storage
   - In-memory data store

6. **API Module** (`com.aurapy.api`)
   - REST-like endpoints
   - Request/response handling
   - Error handling

## Key Classes

### User Management
- `User` - Represents a user account
- `UserRole` - Enum for user roles (STUDENT, STAFF, FACULTY, ADMIN, MERCHANT)
- `UserManager` - Manages user operations

### Transactions
- `Transaction` - Represents a single transaction
- `TransactionStatus` - Enum for transaction states
- `TransactionManager` - Manages transaction operations
- `TransactionValidator` - Validates transaction integrity

### Blockchain
- `Block` - Represents a block in the chain
- `Blockchain` - Main blockchain implementation
- `TransactionPool` - Memory pool for pending transactions

### Security
- `CryptoUtil` - Cryptographic utilities
- `SignatureUtil` - Digital signature utilities

### Main
- `AuraPay` - Main ledger system orchestrator
- `LedgerAPI` - REST-like API interface
- `AuraPayMain` - Interactive CLI application

## Development Guidelines

### Code Style
- Follow Java naming conventions
- Use meaningful variable names
- Add JavaDoc comments for public methods
- Keep methods focused and single-responsibility

### Error Handling
- Use custom exceptions where appropriate
- Log all errors with context
- Provide meaningful error messages

### Testing
- Write unit tests for critical functionality
- Use JUnit 4 for testing
- Test both happy path and edge cases

### Performance Considerations
- Use ConcurrentHashMap for thread-safety
- Implement efficient hashing algorithms
- Optimize block mining with adjustable difficulty
- Monitor transaction pool size

## API Examples

### Creating a Transaction
```java
AuraPay ledger = new AuraPay();

// Create users
User alice = ledger.getUserManager().createUser("Alice", "alice@campus.edu", "STUDENT");
User bob = ledger.getUserManager().createUser("Bob", "bob@campus.edu", "STUDENT");

// Add funds
ledger.getUserManager().addBalance(alice.getId(), 1000.0);

// Create and execute transaction
Transaction tx = ledger.getTransactionManager().createTransaction(
    alice.getId(), bob.getId(), 100.0, "Payment"
);
ledger.getTransactionManager().executeTransaction(tx);
```

### Mining a Block
```java
Block block = ledger.mineNewBlock("MINER_ADDRESS");
System.out.println("Block mined: " + block.getBlockHash());
```

### Validating Blockchain
```java
if (ledger.getBlockchain().isChainValid()) {
    System.out.println("Blockchain is valid");
} else {
    System.out.println("Blockchain integrity compromised");
}
```

## Security Best Practices

1. **Password Security**
   - Passwords are hashed using PBKDF2
   - Salt is generated for each user
   - Never store plain passwords

2. **Transaction Integrity**
   - All transactions are hashed
   - Hashes are verified before processing
   - Merkle root ensures transaction integrity

3. **Blockchain Validation**
   - Chain is validated before accepting new blocks
   - Proof of Work validates mining
   - All previous hashes are verified

4. **User Access Control**
   - Role-based permissions
   - Only ADMIN can approve certain transactions
   - Activity logging

## Troubleshooting

### Common Issues

**Blockchain Validation Fails**
- Check that all blocks have correct previous hash
- Verify mining difficulty matches
- Ensure no blocks were tampered with

**Transaction Execution Fails**
- Verify sender has sufficient balance
- Check user IDs are valid
- Ensure transaction amount is within limits

**Mining is Slow**
- Reduce blockchain difficulty
- Increase system resources
- Check for system load

## Future Enhancements

1. **Database Integration**
   - Replace in-memory storage with database
   - Implement data persistence

2. **Network Layer**
   - Peer-to-peer network implementation
   - Distributed consensus

3. **Advanced Features**
   - Smart contracts
   - Multi-signature transactions
   - Advanced analytics

4. **Performance Optimization**
   - Parallel mining
   - Sharding
   - Caching mechanisms

## Contributing

When contributing to AuraPay:
1. Follow existing code style
2. Add tests for new features
3. Update documentation
4. Create descriptive commit messages
5. Test thoroughly before submitting

## Support

For questions or issues:
- Check existing issues on GitHub
- Review documentation
- Create new issue with detailed description

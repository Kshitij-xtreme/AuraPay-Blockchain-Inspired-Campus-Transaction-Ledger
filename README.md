# AuraPay - Blockchain-Inspired Campus Transaction Ledger

## Overview

AuraPay is a secure, blockchain-inspired digital transaction ledger system designed for campus environments. It enables students and staff to conduct peer-to-peer transactions with cryptographic verification and immutable record-keeping.

## Features

✨ **Core Features**
- 🔐 Blockchain-inspired transaction ledger with SHA-256 hashing
- 👥 User account management with role-based access
- 💳 Peer-to-peer digital transactions
- 📊 Real-time balance tracking
- 🔍 Transaction history and verification
- 🎓 Campus-specific features (student ID, department management)
- 📈 Transaction analytics and reports
- ⚡ Fast and secure payment processing

## Project Structure

```
AuraPay/
├── src/
│   ├── main/
│   │   └── java/com/aurapy/
│   │       ├── blockchain/
│   │       │   ├── Block.java
│   │       │   ├── Blockchain.java
│   │       │   └── TransactionPool.java
│   │       ├── transaction/
│   │       │   ├── Transaction.java
│   │       │   ├── TransactionManager.java
│   │       │   └── TransactionValidator.java
│   │       ├── user/
│   │       │   ├── User.java
│   │       │   ├── UserManager.java
│   │       │   └── UserRole.java
│   │       ├── security/
│   │       │   ├── CryptoUtil.java
│   │       │   └── SignatureUtil.java
│   │       ├── storage/
│   │       │   ├── StorageManager.java
│   │       │   └── DataStore.java
│   │       ├── api/
│   │       │   └── LedgerAPI.java
│   │       ├── AuraPay.java
│   │       └── AuraPayMain.java
│   └── test/
│       └── java/com/aurapy/
│           └── (test files)
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Kshitij-xtreme/AuraPay-Blockchain-Inspired-Campus-Transaction-Ledger.git
cd AuraPay-Blockchain-Inspired-Campus-Transaction-Ledger
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn exec:java
```

## Usage

### Basic Example

```java
import com.aurapy.AuraPay;
import com.aurapy.user.User;
import com.aurapy.transaction.Transaction;

public class Example {
    public static void main(String[] args) {
        AuraPay ledger = new AuraPay();
        
        // Create users
        User alice = ledger.getUserManager().createUser("Alice", "alice@campus.edu", "STUDENT");
        User bob = ledger.getUserManager().createUser("Bob", "bob@campus.edu", "STUDENT");
        
        // Add funds
        ledger.getUserManager().addBalance(alice.getId(), 1000.0);
        
        // Create transaction
        Transaction tx = ledger.getTransactionManager().createTransaction(
            alice.getId(), bob.getId(), 100.0, "Library payment"
        );
        
        // Process transaction
        if (ledger.getTransactionManager().executeTransaction(tx)) {
            System.out.println("Transaction successful!");
        }
    }
}
```

## API Documentation

### LedgerAPI

#### User Management
- `createUser(name, email, role)` - Create new user
- `getUser(userId)` - Retrieve user details
- `addBalance(userId, amount)` - Add funds to account
- `getBalance(userId)` - Get current balance

#### Transactions
- `createTransaction(from, to, amount, description)` - Create new transaction
- `executeTransaction(transaction)` - Process transaction
- `getTransactionHistory(userId)` - Get user's transaction history
- `verifyTransaction(transactionId)` - Verify transaction integrity

#### Blockchain
- `getBlockchain()` - Access the blockchain
- `mineBlock()` - Mine pending transactions
- `validateChain()` - Validate blockchain integrity

## Architecture

### Blockchain Component
Implements a blockchain-inspired ledger with:
- SHA-256 hashing for block integrity
- Merkle tree structure for transaction verification
- Proof of Work concepts
- Immutable transaction records

### Security
- HMAC-SHA256 for transaction signing
- Password hashing with salt
- Transaction verification
- Fraud detection mechanisms

### Data Storage
- In-memory storage with serialization
- JSON-based data persistence
- Transaction log archiving

## Performance

- **Transaction Processing**: < 100ms average
- **Block Mining**: Adjustable difficulty
- **Storage**: Efficient in-memory with optional persistence
- **Scalability**: Supports 10,000+ transactions per session

## Security Considerations

⚠️ **Important**: This is an educational project designed for campus environments. For production use:
- Implement proper database backend
- Add SSL/TLS encryption
- Implement OAuth2 authentication
- Use hardware wallets for key management
- Conduct security audits

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

MIT License - See LICENSE file for details

## Authors

- **Kshitij** - Initial development

## Support

For issues and questions, please open an issue on GitHub.

---

**Version**: 1.0.0  
**Last Updated**: May 2026
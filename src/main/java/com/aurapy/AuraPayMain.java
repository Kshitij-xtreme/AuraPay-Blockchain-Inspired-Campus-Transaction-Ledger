package com.aurapy;

import com.aurapy.api.LedgerAPI;
import com.aurapy.user.UserRole;
import com.aurapy.user.User;
import com.aurapy.transaction.Transaction;
import com.aurapy.blockchain.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main entry point for AuraPay
 */
public class AuraPayMain {
    private static final Logger logger = LoggerFactory.getLogger(AuraPayMain.class);
    private static AuraPay auraPay;
    private static LedgerAPI api;
    private static Scanner scanner;

    public static void main(String[] args) {
        logger.info("Starting AuraPay - Blockchain-Inspired Campus Transaction Ledger");
        
        // Initialize system
        auraPay = new AuraPay();
        api = auraPay.getAPI();
        scanner = new Scanner(System.in);

        // Display welcome message
        displayWelcome();

        // Run interactive menu
        runMenu();

        scanner.close();
        logger.info("AuraPay shutting down");
    }

    private static void displayWelcome() {
        System.out.println("\n" +
                "╔═══════════════════════════════════════════════════════════╗\n" +
                "║         AuraPay - Campus Transaction Ledger               ║\n" +
                "║      Blockchain-Inspired Digital Payment System          ║\n" +
                "╚═══════════════════════════════════════════════════════════╝\n");
    }

    private static void runMenu() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    handleUserManagement();
                    break;
                case 2:
                    handleTransactions();
                    break;
                case 3:
                    handleBlockchain();
                    break;
                case 4:
                    displaySystemStatus();
                    break;
                case 5:
                    running = false;
                    System.out.println("Thank you for using AuraPay!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n┌─ Main Menu ─────────────────────────────────┐");
        System.out.println("│ 1. User Management                          │");
        System.out.println("│ 2. Transactions                             │");
        System.out.println("│ 3. Blockchain                               │");
        System.out.println("│ 4. System Status                            │");
        System.out.println("│ 5. Exit                                     │");
        System.out.println("└─────────────────────────────────────────────┘");
        System.out.print("Select option: ");
    }

    private static void handleUserManagement() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n┌─ User Management ──────────────────────────┐");
            System.out.println("│ 1. Create User                              │");
            System.out.println("│ 2. View User                                │");
            System.out.println("│ 3. Add Balance                              │");
            System.out.println("│ 4. View All Users                           │");
            System.out.println("│ 5. Back                                     │");
            System.out.println("└─────────────────────────────────────────────┘");
            System.out.print("Select option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    viewUser();
                    break;
                case 3:
                    addBalance();
                    break;
                case 4:
                    viewAllUsers();
                    break;
                case 5:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void handleTransactions() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n┌─ Transactions ─────────────────────────────┐");
            System.out.println("│ 1. Create Transaction                       │");
            System.out.println("│ 2. View Transaction                         │");
            System.out.println("│ 3. Transaction History                      │");
            System.out.println("│ 4. All Transactions                         │");
            System.out.println("│ 5. Back                                     │");
            System.out.println("└─────────────────────────────────────────────┘");
            System.out.print("Select option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    createTransaction();
                    break;
                case 2:
                    viewTransaction();
                    break;
                case 3:
                    viewTransactionHistory();
                    break;
                case 4:
                    viewAllTransactions();
                    break;
                case 5:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void handleBlockchain() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n┌─ Blockchain ───────────────────────────────┐");
            System.out.println("│ 1. Mine Block                               │");
            System.out.println("│ 2. Blockchain Info                          │");
            System.out.println("│ 3. Validate Blockchain                      │");
            System.out.println("│ 4. View Block                               │");
            System.out.println("│ 5. Back                                     │");
            System.out.println("└─────────────────────────────────────────────┘");
            System.out.print("Select option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    mineBlock();
                    break;
                case 2:
                    viewBlockchainInfo();
                    break;
                case 3:
                    validateBlockchain();
                    break;
                case 4:
                    viewBlock();
                    break;
                case 5:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void createUser() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter role (STUDENT/STAFF/FACULTY/ADMIN): ");
        String role = scanner.nextLine();

        Map<String, Object> response = api.createUser(name, email, role);
        printResponse(response);
    }

    private static void viewUser() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        Map<String, Object> response = api.getUser(userId);
        printResponse(response);
    }

    private static void addBalance() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = getDoubleInput();
        Map<String, Object> response = api.addBalance(userId, amount);
        printResponse(response);
    }

    private static void viewAllUsers() {
        Map<String, Object> response = api.getAllUsers();
        printResponse(response);
    }

    private static void createTransaction() {
        System.out.print("Enter sender user ID: ");
        String fromId = scanner.nextLine();
        System.out.print("Enter recipient user ID: ");
        String toId = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = getDoubleInput();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        Map<String, Object> response = api.createTransaction(fromId, toId, amount, description);
        printResponse(response);
    }

    private static void viewTransaction() {
        System.out.print("Enter transaction ID: ");
        String txId = scanner.nextLine();
        Map<String, Object> response = api.getTransaction(txId);
        printResponse(response);
    }

    private static void viewTransactionHistory() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        Map<String, Object> response = api.getTransactionHistory(userId);
        printResponse(response);
    }

    private static void viewAllTransactions() {
        Map<String, Object> response = api.getAllTransactions();
        printResponse(response);
    }

    private static void mineBlock() {
        System.out.print("Enter miner address: ");
        String minerAddress = scanner.nextLine();
        Map<String, Object> response = api.mineBlock(minerAddress);
        printResponse(response);
    }

    private static void viewBlockchainInfo() {
        Map<String, Object> response = api.getBlockchainInfo();
        printResponse(response);
    }

    private static void validateBlockchain() {
        Map<String, Object> response = api.validateBlockchain();
        printResponse(response);
    }

    private static void viewBlock() {
        System.out.print("Enter block index: ");
        int index = getIntInput();
        Map<String, Object> response = api.getBlock(index);
        printResponse(response);
    }

    private static void displaySystemStatus() {
        System.out.println("\n" + auraPay.getStatus());
    }

    private static void printResponse(Map<String, Object> response) {
        System.out.println("\n" + response);
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

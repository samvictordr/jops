package com.sece;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankingServices {

    private Connection connection;

    public BankingServices() {
        try {
            connection = DBConnect.getConnection();
            createTables();  // Create the accounts and transaction tables
            createDefaultAdmin();  // Add a default admin user
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        // SQL to create the accounts table
        String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts (" +
                                     "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                                     "username VARCHAR(50) NOT NULL UNIQUE, " +
                                     "password VARCHAR(255) NOT NULL, " +
                                     "balance DOUBLE DEFAULT 0, " +
                                     "account_type VARCHAR(20) NOT NULL, " +
                                     "email VARCHAR(50), " +
                                     "phone VARCHAR(15), " +
                                     "address VARCHAR(100), " +
                                     "status VARCHAR(20) DEFAULT 'active'" +
                                     ")";

        // SQL to create the transaction table
        String createTransactionTable = "CREATE TABLE IF NOT EXISTS transaction (" +
                                        "trans_id INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "account_id INT, " +
                                        "sender_id INT, " +
                                        "receiver_id INT, " +
                                        "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                        "trans_type VARCHAR(20), " +
                                        "trans_amount DOUBLE, " +
                                        "account_balance DOUBLE, " +
                                        "FOREIGN KEY (account_id) REFERENCES accounts(ID)" +
                                        ")";

        try (PreparedStatement stmt1 = connection.prepareStatement(createAccountsTable);
             PreparedStatement stmt2 = connection.prepareStatement(createTransactionTable)) {
            stmt1.executeUpdate();
            stmt2.executeUpdate();
            System.out.println("Database tables created successfully.");
        }
    }

    private void createDefaultAdmin() {
        // Check if the default admin user already exists
        String checkAdminQuery = "SELECT * FROM accounts WHERE username = 'sam'";
        String insertAdminQuery = "INSERT INTO accounts (username, password, account_type) VALUES (?, ?, ?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkAdminQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertAdminQuery)) {
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {  // No admin found, insert a new one
                insertStmt.setString(1, "sam");
                insertStmt.setString(2, "1434");
                insertStmt.setString(3, "admin");
                insertStmt.executeUpdate();
                System.out.println("Default admin account created.");
            } else {
                System.out.println("Default admin account already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean adminLogin(String username, String password) {
        String query = "SELECT * FROM accounts WHERE username = ? AND password = ? AND account_type = 'admin'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean customerLogin(String username, String password) {
        String query = "SELECT * FROM accounts WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createAccount(String username, String password, double balance, String accountType) {
        String query = "INSERT INTO accounts (username, password, balance, account_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setDouble(3, balance);
            stmt.setString(4, accountType);
            stmt.executeUpdate();
            System.out.println("Account created successfully for " + username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     // Method to fetch and print all transactions
    public void printTransactions() {
    String query = "SELECT * FROM transaction";
    
    try (PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        // Print header with fixed width for each column
        System.out.printf("%-15s %-12s %-10s %-12s %-25s %-10s %-12s %-15s\n", 
                          "Transaction ID", "Account ID", "Sender ID", "Receiver ID", 
                          "Date", "Type", "Amount", "Account Balance");

        // Print transaction data with the same fixed width for each column
        while (rs.next()) {
            int transId = rs.getInt("trans_id");
            int accountId = rs.getInt("account_id");
            int senderId = rs.getInt("sender_id");
            int receiverId = rs.getInt("receiver_id");
            String date = rs.getString("date");
            String transType = rs.getString("trans_type");
            double transAmount = rs.getDouble("trans_amount");
            double accountBalance = rs.getDouble("account_balance");

            // Using String.format for alignment
            System.out.printf("%-15d %-12d %-10d %-12d %-25s %-10s %-12.2f %-15.2f\n", 
                              transId, accountId, senderId, receiverId, date, transType, 
                              transAmount, accountBalance);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public void checkBalance(String username) {
        String query = "SELECT balance FROM accounts WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Balance: " + rs.getDouble("balance"));
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void withdraw(String username, double amount) {
    String query = "UPDATE accounts SET balance = balance - ? WHERE username = ? AND balance >= ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setDouble(1, amount);
        stmt.setString(2, username);
        stmt.setDouble(3, amount);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Withdrawn: " + amount);

            // Log the transaction
            String transQuery = "INSERT INTO transaction (account_id, sender_id, trans_type, trans_amount, account_balance) " +
                                "SELECT ID, ID, 'withdraw', ?, balance FROM accounts WHERE username = ?";
            try (PreparedStatement transStmt = connection.prepareStatement(transQuery)) {
                transStmt.setDouble(1, amount);
                transStmt.setString(2, username);
                transStmt.executeUpdate();
            }
        } else {
            System.out.println("Insufficient balance.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public void transfer(String sender, String receiver, double amount) {
    withdraw(sender, amount);
    
    String query = "UPDATE accounts SET balance = balance + ? WHERE username = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setDouble(1, amount);
        stmt.setString(2, receiver);
        stmt.executeUpdate();
        System.out.println("Transferred " + amount + " to " + receiver);

        // Log the transaction for sender
        String senderTransQuery = "INSERT INTO transaction (account_id, sender_id, receiver_id, trans_type, trans_amount, account_balance) " +
                                   "SELECT ID, ID, (SELECT ID FROM accounts WHERE username = ?), 'transfer', ?, balance FROM accounts WHERE username = ?";
        try (PreparedStatement senderTransStmt = connection.prepareStatement(senderTransQuery)) {
            senderTransStmt.setString(1, receiver);
            senderTransStmt.setDouble(2, amount);
            senderTransStmt.setString(3, sender);
            senderTransStmt.executeUpdate();
        }

        // Log the transaction for receiver
        String receiverTransQuery = "INSERT INTO transaction (account_id, sender_id, receiver_id, trans_type, trans_amount, account_balance) " +
                                    "SELECT ID, (SELECT ID FROM accounts WHERE username = ?), ID, 'transfer', ?, balance FROM accounts WHERE username = ?";
        try (PreparedStatement receiverTransStmt = connection.prepareStatement(receiverTransQuery)) {
            receiverTransStmt.setString(1, sender);
            receiverTransStmt.setDouble(2, amount);
            receiverTransStmt.setString(3, receiver);
            receiverTransStmt.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}

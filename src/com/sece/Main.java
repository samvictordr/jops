package com.sece;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BankingServices bankService = new BankingServices();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Banking App!");
        boolean running = true;

        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Admin Login");
            System.out.println("2. Customer Login");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Admin Username: ");
                    String adminUsername = scanner.nextLine();
                    System.out.print("Enter Admin Password: ");
                    String adminPassword = scanner.nextLine();
                    if (bankService.adminLogin(adminUsername, adminPassword)) {
                        adminDashboard(bankService, scanner);
                    } else {
                        System.out.println("Invalid admin credentials.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Account Username: ");
                    String customerUsername = scanner.nextLine();
                    System.out.print("Enter Account Password: ");
                    String customerPassword = scanner.nextLine();
                    if (bankService.customerLogin(customerUsername, customerPassword)) {
                        customerDashboard(bankService, customerUsername, scanner);
                    } else {
                        System.out.println("Invalid customer credentials.");
                    }
                    break;

                case 0:
                    running = false;
                    System.out.println("Exiting the application.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void adminDashboard(BankingServices bankService, Scanner scanner) {
        boolean adminRunning = true;

        while (adminRunning) {
            System.out.println("\nAdmin Dashboard:");
            System.out.println("1. Create Account");
            System.out.println("2. Check Transactions");
            System.out.println("3. Check Account Balance");
            System.out.println("0. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter New Account Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter Initial Balance: ");
                    double balance = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter Account Type: ");
                    String accountType = scanner.nextLine();

                    bankService.createAccount(username, password, balance, accountType);
                    break;

                case 2:
                    bankService.printTransactions();
                    break;

                case 3:
                    System.out.print("Enter Account Username to Check Balance: ");
                    String accountUsername = scanner.nextLine();
                    bankService.checkBalance(accountUsername);
                    break;

                case 0:
                    adminRunning = false;
                    System.out.println("Logging out of admin dashboard.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void customerDashboard(BankingServices bankService, String username, Scanner scanner) {
        boolean customerRunning = true;

        while (customerRunning) {
            System.out.println("\nCustomer Dashboard:");
            System.out.println("1. Withdraw");
            System.out.println("2. Transfer");
            System.out.println("0. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Amount to Withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine();
                    bankService.withdraw(username, withdrawAmount);
                    break;

                case 2:
                    System.out.print("Enter Receiver's Username: ");
                    String receiver = scanner.nextLine();
                    System.out.print("Enter Transfer Amount: ");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine();
                    bankService.transfer(username, receiver, transferAmount);
                    break;

                case 0:
                    customerRunning = false;
                    System.out.println("Logging out of customer dashboard.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

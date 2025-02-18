
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

// User class to store user details and transaction history
class User {
    String username;
    String password;
    List<Transaction> transactions;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.transactions = new ArrayList<>();
    }
}

// Transaction class to store payment details
class Transaction {
    String transactionID;
    double amount;
    String paymentMethod;
    
    public Transaction(String transactionID, double amount, String paymentMethod) {
        this.transactionID = transactionID;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
}

public class AdvancedPaymentSystem {
    static List<User> users = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static User currentUser = null;
    
    // Method to register a new user
    public static void register() {
        System.out.print("Enter new username: ");
        String username = scanner.next();
        
        System.out.print("Enter new password: ");
        String password = scanner.next();
        
        users.add(new User(username, password));
        System.out.println("Registration successful. Please login.");
    }
    
    // Method to login a user
    public static boolean login() {
        System.out.print("Enter username: ");
        String username = scanner.next();
        
        System.out.print("Enter password: ");
        String password = scanner.next();
        
        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                currentUser = user;
                System.out.println("Login successful. Welcome, " + currentUser.username);
                return true;
            }
        }
        
        System.out.println("Invalid username or password.");
        return false;
    }
    
    // Luhn Algorithm for card validation
    public static boolean validateCardNumber(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
    
    // Generate a unique transaction ID
    public static String generateTransactionID() {
        return UUID.randomUUID().toString();
    }
    
    // Display transaction history for the logged-in user
    public static void showTransactionHistory() {
        if (currentUser.transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        
        System.out.println("\n--- Transaction History ---");
        for (Transaction t : currentUser.transactions) {
            System.out.println("Transaction ID: " + t.transactionID);
            System.out.println("Amount: " + t.amount);
            System.out.println("Payment Method: " + t.paymentMethod);
            System.out.println("-----------------------------");
        }
    }
    
    // Main payment method
    public static void makePayment() {
        System.out.print("Enter the amount: ");
        double amount = scanner.nextDouble();
        
        if (amount <= 0) {
            System.out.println("Invalid amount. Payment failed.");
            return;
        }
        
        double tax = amount * 0.05;
        double totalAmount = amount + tax;
        
        System.out.println("\nAmount: " + amount);
        System.out.println("Tax (5%): " + tax);
        System.out.println("Total Amount: " + totalAmount);
        
        System.out.println("\nSelect Payment Method:");
        System.out.println("1. Credit Card");
        System.out.println("2. Debit Card");
        System.out.println("3. UPI");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();
        
        boolean paymentSuccess = false;
        
        switch (choice) {
            case 1:
            case 2:
                System.out.print("Enter Card Number (16 digits): ");
                String cardNumber = scanner.next();
                
                if (validateCardNumber(cardNumber)) {
                    System.out.print("Enter CVV: ");
                    String cvv = scanner.next();
                    if (cvv.length() == 3) {
                        paymentSuccess = true;
                    } else {
                        System.out.println("Invalid CVV.");
                    }
                } else {
                    System.out.println("Invalid Card Number.");
                }
                break;
                
            case 3:
                System.out.print("Enter UPI ID: ");
                String upiID = scanner.next();
                
                if (upiID.contains("@") && upiID.length() > 5) {
                    paymentSuccess = true;
                } else {
                    System.out.println("Invalid UPI ID.");
                }
                break;
                
            default:
                System.out.println("Invalid payment method.");
                break;
        }
        
        if (paymentSuccess) {
            String transactionID = generateTransactionID();
            System.out.println("\nPayment Successful!");
            System.out.println("Transaction ID: " + transactionID);
            
            currentUser.transactions.add(new Transaction(transactionID, totalAmount, 
                (choice == 3 ? "UPI" : "Card")));
        } else {
            System.out.println("\nPayment Failed. Try Again.");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Welcome to Advanced Payment System");
        
        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Make Payment");
            System.out.println("4. View Transaction History");
            System.out.println("5. Logout");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    if (!login()) {
                        System.out.println("Login failed. Try again.");
                    }
                    break;
                case 3:
                    if (currentUser != null) {
                        makePayment();
                    } else {
                        System.out.println("Please login first.");
                    }
                    break;
                case 4:
                    if (currentUser != null) {
                        showTransactionHistory();
                    } else {
                        System.out.println("Please login first.");
                    }
                    break;
                case 5:
                    currentUser = null;
                    System.out.println("Logged out successfully.");
                    break;
                case 6:
                    System.out.println("Exiting... Thank you for using our service.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

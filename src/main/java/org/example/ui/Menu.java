
package org.example.ui;

import org.example.dao.UserDao;
import org.example.model.User;

import java.sql.SQLException;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final UserDao userDao = new UserDao();

    public void start() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("       CLINIC APPOINTMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("[1] Register Account");
            System.out.println("[2] Login");
            System.out.println("[3] Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> registerAccount();
                    case "2" -> loginAccount();
                    case "3" -> {
                        System.out.println("Application closed. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private void registerAccount() throws SQLException {
        System.out.println("\n--- REGISTER CLINIC ACCOUNT ---");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (username.isBlank() || password.isBlank()) {
            System.out.println("Username and password are required.");
            return;
        }

        System.out.println("\nSelect role:");
        System.out.println("[1] ADMIN");
        System.out.println("[2] RECEPTIONIST");
        System.out.println("[3] DOCTOR");
        System.out.print("Enter choice: ");

        String roleChoice = scanner.nextLine().trim();
        String role;

        switch (roleChoice) {
            case "1" -> role = "ADMIN";
            case "2" -> role = "RECEPTIONIST";
            case "3" -> role = "DOCTOR";
            default -> {
                System.out.println("Invalid role selection.");
                return;
            }
        }

        boolean registered =
                userDao.register(username, password, role);

        if (registered) {
            System.out.println("\nAccount registered successfully!");
            System.out.println("Username: " + username);
            System.out.println("Role: " + role);
            System.out.println("Status: PENDING");
            System.out.println("Your account is awaiting approval.");
        } else {
            System.out.println("Username already exists.");
        }
    }

    private void loginAccount() throws SQLException {
        System.out.println("\n--- CLINIC SYSTEM LOGIN ---");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userDao.login(username, password);

        if (user == null) {
            System.out.println("Invalid username or password.");
            return;
        }

        if (!user.status().equals("APPROVED")) {
            System.out.println("Your account is " + user.status() + ".");
            System.out.println("Please contact the clinic administrator.");
            return;
        }

        System.out.println("\nLogin successful!");
        System.out.println("Welcome, " + user.username());
        System.out.println("Role: " + user.roles());

        showDashboard(user);
    }

    private void showDashboard(User user) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n========================================");
            System.out.println("          CLINIC DASHBOARD");
            System.out.println("========================================");
            System.out.println("User: " + user.username());
            System.out.println("Role: " + user.roles());

            switch (user.roles()) {
                case "ADMIN" -> {
                    System.out.println("[1] Manage User Accounts");
                    System.out.println("[2] View Clinic Information");
                    System.out.println("[3] Logout");
                }
                case "RECEPTIONIST" -> {
                    System.out.println("[1] Register Patient");
                    System.out.println("[2] Manage Appointments");
                    System.out.println("[3] Logout");
                }
                case "DOCTOR" -> {
                    System.out.println("[1] View Appointments");
                    System.out.println("[2] Consultation / Diagnosis");
                    System.out.println("[3] Logout");
                }
                default -> {
                    System.out.println("Unknown role.");
                    return;
                }
            }

            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("3")) {
                System.out.println("Logged out successfully.");
                loggedIn = false;
                continue;
            }

            if (choice.equals("1") || choice.equals("2")) {
                System.out.println(
                        "This feature still needs to be connected "
                                + "to its database operations."
                );
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}

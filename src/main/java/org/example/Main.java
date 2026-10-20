
package org.example;

import org.example.dao.DatabaseConnection;
import org.example.ui.Menu;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            DatabaseConnection.initializeDatabase();

            Menu menu = new Menu();
            menu.start();

        } catch (SQLException e) {
            System.err.println("Could not start the application.");
            System.err.println("Database error: " + e.getMessage());
        }
    }
}

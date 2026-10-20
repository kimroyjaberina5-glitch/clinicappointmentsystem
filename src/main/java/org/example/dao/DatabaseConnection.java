
package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:users.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    roles TEXT NOT NULL
                        CHECK (roles IN (
                            'ADMIN',
                            'RECEPTIONIST',
                            'DOCTOR'
                        )),
                    status TEXT NOT NULL DEFAULT 'PENDING'
                        CHECK (status IN (
                            'PENDING',
                            'APPROVED',
                            'REJECTED'
                        ))
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);
        }

        System.out.println("Database connected successfully!");
        System.out.println("Users table is ready!");
    }
}

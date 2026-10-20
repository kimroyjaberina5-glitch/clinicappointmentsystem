
package org.example.dao;

import org.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class UserDao {

    private static final Set<String> VALID_ROLES =
            Set.of("ADMIN", "RECEPTIONIST", "DOCTOR");

    public boolean register(
            String username,
            String password,
            String role) throws SQLException {

        if (!VALID_ROLES.contains(role)) {
            throw new IllegalArgumentException("Invalid account role.");
        }

        String sql = """
                INSERT INTO users (username, password, roles, status)
                VALUES (?, ?, ?, 'PENDING')
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            if (e.getErrorCode() == 19
                    && e.getMessage() != null
                    && e.getMessage().toLowerCase()
                    .contains("unique")) {
                return false;
            }

            throw e;
        }
    }

    public User login(
            String username,
            String password) throws SQLException {

        String sql = """
                SELECT id, username, password, roles, status
                FROM users
                WHERE username = ? AND password = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new User(
                            result.getInt("id"),
                            result.getString("username"),
                            result.getString("password"),
                            result.getString("roles"),
                            result.getString("status")
                    );
                }
            }
        }

        return null;
    }
}

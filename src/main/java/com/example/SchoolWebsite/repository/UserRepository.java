package com.example.SchoolWebsite.repository;

import com.example.SchoolWebsite.model.UserAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserAccount findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        String trimmed = username.trim();

        // 1. Direct username check
        String sql = "SELECT Username, Password, Role FROM User_Account WHERE Username = ?";
        List<UserAccount> list = jdbcTemplate.query(sql, (rs, rowNum) -> new UserAccount(
            rs.getString("Username"),
            rs.getString("Password"),
            rs.getString("Role")
        ), trimmed);

        if (!list.isEmpty()) {
            return list.get(0);
        }

        // 2. Case-insensitive username check
        List<UserAccount> listCase = jdbcTemplate.query(
            "SELECT Username, Password, Role FROM User_Account WHERE UPPER(Username) = UPPER(?)",
            (rs, rowNum) -> new UserAccount(
                rs.getString("Username"),
                rs.getString("Password"),
                rs.getString("Role")
            ), trimmed
        );

        if (!listCase.isEmpty()) {
            return listCase.get(0);
        }

        // 3. Fallback: Check if teacher logged in via Email
        List<String> teacherIds = jdbcTemplate.query(
            "SELECT Employee_ID FROM Teacher WHERE UPPER(Email) = UPPER(?)",
            (rs, rowNum) -> rs.getString("Employee_ID"),
            trimmed
        );

        if (!teacherIds.isEmpty()) {
            return findByUsername(teacherIds.get(0));
        }

        return null;
    }

    public void createOrUpdate(String username, String password, String role) {
        String sql = "INSERT INTO User_Account (Username, Password, Role) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE Password = VALUES(Password), Role = VALUES(Role)";
        jdbcTemplate.update(sql, username, password, role);
    }

    public void delete(String username) {
        jdbcTemplate.update("DELETE FROM User_Account WHERE Username = ?", username);
    }
}
